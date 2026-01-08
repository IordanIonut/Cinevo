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
    private final com.cinovo.backend.TMDB.Service service;

    public MediaResolver(@Lazy MediaService mediaService, CreditService creditService, VideoService videoService,
            WatchProviderService watchProviderService, ImageService imageService, com.cinovo.backend.TMDB.Service service)
    {
        this.mediaService = mediaService;
        this.creditService = creditService;
        this.videoService = videoService;
        this.imageService = imageService;
        this.watchProviderService = watchProviderService;
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
        MediaExternalIdResponse mediaExternalIdsResponse =
                type.equals(MediaType.MOVIE) ? service.getMovieExternalIds(mediaId) : service.getTvExternalIds(mediaId);

        media.setImdb_id(mediaExternalIdsResponse.getImdb_id());
        media.setFreebase_mid(mediaExternalIdsResponse.getFreebase_mid());
        media.setFreebase_id(mediaExternalIdsResponse.getFreebase_id());
        media.setTvdb_id(mediaExternalIdsResponse.getTvdb_id());
        media.setTvrage_id(mediaExternalIdsResponse.getTvrage_id());
        media.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
        media.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
        media.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
        media.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());

        if(media.getImages() != null)
        {
            media.getImages().clear();
            media.getImages().addAll(imageService.findByTmdbIdAndMediaType(mediaId, media.getType()));
        }
        if(media.getVideo() != null)
        {
            media.getVideos().clear();
            media.getVideos().addAll(videoService.findByMediaTmdbId(mediaId, media.getType()));
        }
        if(media.getWatch_providers() != null)
        {
            media.getWatch_providers().clear();
            media.getWatch_providers().addAll(this.watchProviderService.findByMediaTmdbIdAndMediaType(mediaId, media.getType()));
        }

        if(media.getSeasons() != null)
        {
            for(Media.Season season : media.getSeasons())
            {
                Media.Season ses = mediaService.getSeasonBySeasonTmdbId(season.getTmdb_id());

                mediaExternalIdsResponse = service.getTvSeasonExternalId(media.getTmdb_id(), ses.getSeason_number());
                ses.setImdb_id(mediaExternalIdsResponse.getImdb_id());
                ses.setFreebase_mid(mediaExternalIdsResponse.getFreebase_mid());
                ses.setFreebase_id(mediaExternalIdsResponse.getFreebase_id());
                ses.setTvdb_id(mediaExternalIdsResponse.getTvdb_id());
                ses.setTvrage_id(mediaExternalIdsResponse.getTvrage_id());
                ses.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
                ses.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
                ses.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
                ses.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());

                if(ses.getWatch_providers() != null)
                {
                    ses.getWatch_providers().clear();
                    ses.getWatch_providers()
                            .addAll(watchProviderService.findByMediaTmdbIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(),
                                    MediaType.TV_SEASON));
                }

                if(ses.getVideos() != null)
                {
                    ses.getVideos().clear();
                    ses.getVideos().addAll(videoService.findByMediaTmdbIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(),
                            MediaType.TV_SEASON));
                }

                if(ses.getImages() != null)
                {
                    ses.getImages().clear();
                    ses.getImages().addAll(imageService.findImageBySeasonIdAndSeasonNumber(media.getTmdb_id(), season.getSeason_number(),
                            MediaType.TV_SEASON));
                }

                if(ses.getEpisodes() != null)
                {
                    for(Media.Season.Episode episode : ses.getEpisodes())
                    {
                        Media.Season.Episode epi = mediaService.findEpisodeByTmdbId(episode.getTmdb_id());

                        mediaExternalIdsResponse =
                                service.getTvSeasonEpisodeExternal(media.getTmdb_id(), ses.getSeason_number(), epi.getEpisode_number());
                        epi.setImdb_id(mediaExternalIdsResponse.getImdb_id());
                        epi.setFreebase_mid(mediaExternalIdsResponse.getFreebase_mid());
                        epi.setFreebase_id(mediaExternalIdsResponse.getFreebase_id());
                        epi.setTvdb_id(mediaExternalIdsResponse.getTvdb_id());
                        epi.setTvrage_id(mediaExternalIdsResponse.getTvrage_id());
                        epi.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
                        epi.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
                        epi.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
                        epi.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());

                        if(epi.getVideos() != null)
                        {
                            epi.getVideos().clear();
                            epi.getVideos()
                                    .addAll(videoService.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media.getTmdb_id(), ses.getSeason_number(),
                                            episode.getEpisode_number(), MediaType.TV_EPISODE));
                        }

                        if(epi.getImages() != null)
                        {
                            epi.getImages().clear();
                            epi.getImages()
                                    .addAll(imageService.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media.getTmdb_id(), ses.getSeason_number(),
                                            episode.getEpisode_number(), MediaType.TV_EPISODE));
                        }
                        epi.setSeason(ses);
                    }
                }
                ses.setMedia(media);
            }
        }
        mediaService.saveAndUpdate(media);
    }
}
