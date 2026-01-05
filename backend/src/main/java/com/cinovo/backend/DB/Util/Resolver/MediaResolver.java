package com.cinovo.backend.DB.Util.Resolver;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Service.*;
import com.cinovo.backend.DB.Util.Shared;
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

    public MediaResolver(@Lazy MediaService mediaService, CreditService creditService, VideoService videoService,
            WatchProviderService watchProviderService, ImageService imageService)
    {
        this.mediaService = mediaService;
        this.creditService = creditService;
        this.videoService = videoService;
        this.imageService = imageService;
        this.watchProviderService = watchProviderService;
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

    //    @Async("customExecutorMedia")
    //    @Transactional
    //    public void generateDateAsync(final Integer media_id, final MediaType type) throws Exception
    //    {
    //        Media media = mediaService.getMediaByIdAndType(media_id, type);
    //        media.setImages(imageService.findImageByMediaIdAndMediaType(media_id, media.getType()));
    //        mediaService.saveAndUpdate(media);
    //
    //        media.setVideos(videoService.findVideosByMovieIdAndMediaType(media_id, media.getType()));
    //        mediaService.saveAndUpdate(media);
    //
    //        media.setWatch_providers(watchProviderService.findWatchProviderByMediaIdAndType(media_id, media.getType()));
    //        mediaService.saveAndUpdate(media);
    //
    //        if(media.getSeasons() != null)
    //        {
    //            List<Media.Season> seasons = new ArrayList<>();
    //            for(Media.Season season : media.getSeasons())
    //            {
    //                Media.Season ses = mediaService.getSeasonById(season.getId());
    //                ses.setWatch_providers(
    //                        watchProviderService.findWatchProviderBySeasonIdAndType(media.getId(), season.getSeason_number(), MediaType.TV_SEASON));
    //                ses.setVideos(videoService.findVideosBySeriesIdAndMediaType(media.getId(), season.getSeason_number(), MediaType.TV_SEASON));
    //                ses.setImages(imageService.findImageBySeasonIdAndSeasonNumber(media.getId(), season.getSeason_number(), MediaType.TV_SEASON));
    //
    //                List<Media.Season.Episode> episodes = new ArrayList<>();
    //                for(Media.Season.Episode episode : ses.getEpisodes())
    //                {
    //                    Media.Season.Episode epi = mediaService.findEpisodeById(episode.getId());
    //                    epi.setVideos(videoService.findVideoBySeriesIdAndSeasonNumberAndEpisodeAndMediaType(media.getId(), ses.getSeason_number(),
    //                            episode.getEpisode_number(), MediaType.TV_EPISODE));
    //                    epi.setImages(imageService.findImageBySeasonIdAndSeasonNumberAndEpisodeAndMediaType(media.getId(), ses.getSeason_number(),
    //                            episode.getEpisode_number(), MediaType.TV_EPISODE));
    //
    //                    episodes.add(epi);
    //                }

    //
    //                ses.setEpisodes(episodes);
    //                seasons.add(ses);
    //            }
    //            media.setSeasons(seasons);
    //        }
    //        mediaService.saveAndUpdate(media);

    ////INFO: have some errors on db when call this maybe can fixed
    //    //            collectCreditsAsync(media_id, type);
    //    }

    //    @Async("customExecutorMedia")
    //    public void collectCreditsAsync(final Integer media_id, final MediaType type) throws Exception
    //    {
    //        Media media = mediaService.getMediaByIdAndType(media_id, type);
    //        media.setCredits(creditService.findCreditByMediaIdAndType(media_id, type));
    //        mediaService.saveAndUpdate(media);
    //    }
}
