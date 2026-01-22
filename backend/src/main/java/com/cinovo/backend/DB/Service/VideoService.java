package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.SiteType;
import com.cinovo.backend.DB.Model.Enum.VideoType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Video;
import com.cinovo.backend.DB.Repository.VideoRepository;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.MediaVideoResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@JBossLog
public class VideoService implements TMDBLogically<Object, List<Video>>
{
    private final VideoRepository videoRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final MediaService mediaService;

    public VideoService(VideoRepository videoRepository, com.cinovo.backend.TMDB.Service service, @Lazy MediaService mediaService)
    {
        this.videoRepository = videoRepository;
        this.service = service;
        this.mediaService = mediaService;
    }

    public List<Video> findByMediaTmdbId(final Integer media_tmdb_id, final MediaType type) throws Exception
    {
        Optional<List<Video>> videos = this.videoRepository.findByMediaTmdbId(media_tmdb_id);
        if(videos.isEmpty() || videos.get().isEmpty())
        {
            return (List<Video>) this.onConvertTMDB(type + Shared.REGEX + media_tmdb_id);
        }
        return videos.get();
    }

    public List<Video> findByMediaTmdbIdAndSeasonNumber(final Integer media_tmdb_id, final Integer season_number, final MediaType type)
            throws Exception
    {
        Optional<List<Video>> videos = this.videoRepository.findByMediaTmdbIdAndSeasonNumber(media_tmdb_id, season_number);
        if(videos.isEmpty() || videos.get().isEmpty())
        {
            return (List<Video>) this.onConvertTMDB(type + Shared.REGEX + media_tmdb_id + Shared.REGEX + season_number);
        }
        return videos.get();
    }

    public List<Video> findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(final Integer media_tmdb_id, final Integer season_number,
            final Integer episode_number, final MediaType type) throws Exception
    {
        Optional<List<Video>> videos =
                this.videoRepository.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media_tmdb_id, season_number, episode_number);
        if(videos.isEmpty() || videos.get().isEmpty())
        {
            return (List<Video>) this.onConvertTMDB(
                    type + Shared.REGEX + media_tmdb_id + Shared.REGEX + season_number + Shared.REGEX + episode_number);
        }
        return videos.get();
    }

    @Override
    @Transactional
    public List<Video> onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        MediaVideoResponse movieVideoResponse =
                MediaType.valueOf(parts[0]) == MediaType.TV_SEASON
                        ? this.service.getTvSeasonMediaVideo(Shared.onStringParseToInteger(parts[1]), Shared.onStringParseToInteger(parts[2]), null,
                        null)
                        : MediaType.valueOf(parts[0]) == MediaType.TV_EPISODE
                                ? this.service.getTvSeasonEpisodeVideo(Shared.onStringParseToInteger(parts[1]),
                                Shared.onStringParseToInteger(parts[2]), Shared.onStringParseToInteger(parts[3]), null, null)
                                : this.service.getMediaVideo(Shared.onStringParseToInteger(parts[1]), MediaType.valueOf(parts[0]), null, null);
        List<Video> videos = new ArrayList<>();
        Media media = MediaResolver.resolveMedia(mediaService, parts);
        Media.Season season = MediaResolver.resolveSeason(mediaService, parts);
        Media.Season.Episode episode = MediaResolver.resolveEpisode(mediaService, parts);

        if(movieVideoResponse.getResults() != null)
        {
            for(MediaVideoResponse.Video video : movieVideoResponse.getResults())
            {
                this.videoRepository.upsertOrInsert(Shared.generateCinevoId(this.videoRepository.findByTmdbId(video.getId())), video.getIso_639_1(),
                        video.getIso_3166_1(), video.getName(), video.getKey(), SiteType.fromLabel(video.getSite()).name(),
                        VideoType.fromLabel(video.getType()).name(), video.getOfficial(), OffsetDateTime.parse(video.getPublished_at()).toLocalDate(),
                        video.getId(), Shared.idOf(media), Shared.idOf(season), Shared.idOf(episode));

                this.videoRepository.findByTmdbId(video.getId()).ifPresent(videos::add);
            }
        }

        return videos;
    }
}
