package com.cinovo.backend.DB.Util.Resolver;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Service.*;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.TMDB.Response.Common.MediaExternalIdResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@JBossLog
@Service
public class MediaResolver
{
    private final MediaService mediaService;
    private final CreditService creditService;
    private final VideoService videoService;
    private final WatchProviderService watchProviderService;
    private final ImageService imageService;
    private final ExternalService externalService;
    private final com.cinovo.backend.TMDB.Service service;

    public MediaResolver(@Lazy MediaService mediaService, CreditService creditService, VideoService videoService,
            WatchProviderService watchProviderService, ImageService imageService, ExternalService externalService,
            com.cinovo.backend.TMDB.Service service)
    {
        this.mediaService = mediaService;
        this.creditService = creditService;
        this.videoService = videoService;
        this.imageService = imageService;
        this.watchProviderService = watchProviderService;
        this.externalService = externalService;
        this.service = service;
    }

    public static Media resolveMedia(final MediaService mediaService, final String[] parts) throws Exception
    {
        MediaType type = MediaType.valueOf(parts[0]);
        if(type == MediaType.TV_SEASON || type == MediaType.TV_EPISODE)
        {
            return null;
        }
        return mediaService.getMediaByTmdbIdAndMediaType(Shared.onStringParseToInteger(parts[1]), type);
    }

    public static Media.Season resolveSeason(final MediaService mediaService, final String[] parts) throws Exception
    {
        if(MediaType.valueOf(parts[0]) != MediaType.TV_SEASON)
        {
            return null;
        }
        return mediaService.getSeasonByMediaTmdbIdAndSeasonNumber(Shared.onStringParseToInteger(parts[1]), Shared.onStringParseToInteger(parts[2]));
    }

    public static Media.Season.Episode resolveEpisode(final MediaService mediaService, final String[] parts) throws Exception
    {
        if(MediaType.valueOf(parts[0]) != MediaType.TV_EPISODE)
        {
            return null;
        }
        return mediaService.getEpisodeByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(Shared.onStringParseToInteger(parts[1]),
                Shared.onStringParseToInteger(parts[2]), Shared.onStringParseToInteger(parts[3]));
    }

    @Async("customExecutorMedia")
    @Transactional
    public void generateDateAsync(final Integer mediaId, final MediaType type) throws Exception
    {
        Media media = mediaService.getMediaByTmdbIdAndMediaType(mediaId, type);
        this.externalService.setExternalByMediaType(mediaId, media.getCinevo_id(), type, null, null);
        this.imageService.findByTmdbIdAndMediaType(mediaId, type);
        this.videoService.findByMediaTmdbId(mediaId, type);
//        this.watchProviderService.findByMediaTmdbIdAndMediaType(mediaId, type);

        if(media.getSeasons() != null)
        {
            for(Media.Season season : media.getSeasons())
            {
                Media.Season ses = mediaService.getSeasonBySeasonTmdbId(season.getTmdb_id());
                this.externalService.setExternalByMediaType(mediaId, ses.getCinevo_id(), MediaType.TV_SEASON, ses.getSeason_number(), null);
//                this.watchProviderService.findByMediaTmdbIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(), MediaType.TV_SEASON);
                this.videoService.findByMediaTmdbIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(), MediaType.TV_SEASON);
                this.imageService.findImageBySeasonIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(), MediaType.TV_SEASON);

                if(ses.getEpisodes() != null)
                {
                    for(Media.Season.Episode episode : ses.getEpisodes())
                    {
                        Media.Season.Episode epi = mediaService.findEpisodeByTmdbId(episode.getTmdb_id());
                        this.externalService.setExternalByMediaType(mediaId, episode.getCinevo_id(), MediaType.TV_EPISODE, ses.getSeason_number(),
                                epi.getEpisode_number());
                        this.videoService.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media.getTmdb_id(), ses.getSeason_number(),
                                episode.getEpisode_number(), MediaType.TV_EPISODE);
                        this.imageService.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media.getTmdb_id(), ses.getSeason_number(),
                                episode.getEpisode_number(), MediaType.TV_EPISODE);

                    }
                }
            }
        }
    }
}
