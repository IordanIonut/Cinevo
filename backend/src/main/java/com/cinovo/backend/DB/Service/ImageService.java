package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.ImageType;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.ImageView;
import com.cinovo.backend.DB.Repository.ImageRepository;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Schedule.Job;
import com.cinovo.backend.TMDB.Response.CollectionImageResponse;
import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaImagesResponse;
import com.cinovo.backend.TMDB.Response.PeopleImagesResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@JBossLog
public class ImageService implements TMDBLogically<Object, Object>
{
    private final ImageRepository imageRepository;
    private final MediaService mediaService;
    private final PersonService personService;
    private final com.cinovo.backend.TMDB.Service service;

    public ImageService(ImageRepository imageRepository, @Lazy MediaService mediaService, PersonService personService,
            com.cinovo.backend.TMDB.Service service)
    {
        this.imageRepository = imageRepository;
        this.mediaService = mediaService;
        this.personService = personService;
        this.service = service;
    }

    public List<Image> findByTmdbIdAndMediaType(final Integer tmdb_id, final MediaType type) throws Exception
    {
        Optional<List<Image>> image = this.imageRepository.findByTmdbIdAndMediaType(tmdb_id, type.name());
        if(image.isEmpty() || image.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(type + Shared.REGEX + tmdb_id + Shared.REGEX + null + Shared.REGEX + null);
        }
        return image.get();
    }

    public List<Image> findImageBySeasonIdAndSeasonNumber(final Integer series_id, final Integer season_number, final MediaType type) throws Exception
    {
        Optional<List<Image>> images = this.imageRepository.findImageBySeasonIdAndSeasonNumber(series_id, season_number);
        if(images.isEmpty() || images.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(type + Shared.REGEX + series_id + Shared.REGEX + season_number + Shared.REGEX + null);
        }
        return images.get();
    }

    public List<Image> findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(final Integer media_tmdb_id, final Integer season_number,
            final Integer episode_number, final MediaType type) throws Exception
    {
        Optional<List<Image>> images =
                this.imageRepository.findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media_tmdb_id, season_number, episode_number);
        if(images.isEmpty() || images.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(
                    type + Shared.REGEX + media_tmdb_id + Shared.REGEX + season_number + Shared.REGEX + episode_number);
        }
        return images.get();
    }

    public List<ImageView> findImageViewByMediaTypeAndCinevoId(final MediaType media_type,final String media_cinevo_id)
    {
        return this.imageRepository.findImageViewByMediaTypeAndCinevoId(Job.configurationUrlImages, media_type.name(), media_cinevo_id).get();
    }

    @Override
    public Object onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);
        return onConvertTMDB(Shared.onStringParseToInteger(parts[1]), Shared.onStringParseToInteger(parts[2]),
                Shared.onStringParseToInteger(parts[3]), MediaType.valueOf(parts[0]));
    }

    private List<Image> onConvertTMDB(final Integer id, final Integer season_number, final Integer episode_number, final MediaType type)
            throws Exception
    {
        List<Image> images = switch(type)
        {
            case MediaType.MOVIE ->
            {
                MediaImagesResponse response = service.getMovieImages(id);
                Media media = mediaService.getMediaByTmdbIdAndMediaType(response.getId(), MediaType.MOVIE);
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), null, null, media, null, null,
                        null, null, MediaType.MOVIE);
            }
            case MediaType.PERSON ->
            {
                PeopleImagesResponse response = service.getPeopleImages(id);
                Person person = this.personService.findByTmdbId(id);
                yield convertImageResponseToImages(null, null, null, response.getProfiles(), null, null, null, null, person, null, MediaType.PERSON);
            }
            case MediaType.COLLECTION ->
            {
                CollectionImageResponse response = service.getImageCollection(id);
                Integer collection = null; // TODO: implement collection lookup
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), null, null, null, null, null, null, null,
                        collection, MediaType.COLLECTION);
            }
            case MediaType.TV ->
            {
                MediaImagesResponse response = service.getTvImages(id, null, null);
                Media media = mediaService.getMediaByTmdbIdAndMediaType(response.getId(), MediaType.TV);
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), null, null, media, null, null,
                        null, null, MediaType.TV);
            }
            case MediaType.TV_SEASON ->
            {
                MediaImagesResponse response = service.getTvSeasonMediaImage(id, season_number, null, null);
                String[] parts = new String[] { MediaType.TV_SEASON.name(), id + "", season_number + "" };
                Media media = MediaResolver.resolveMedia(mediaService, parts);
                Media.Season season = MediaResolver.resolveSeason(mediaService, parts);

                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), null, null, media, season,
                        null, null, null, MediaType.TV_SEASON);
            }
            case MediaType.TV_EPISODE ->
            {
                MediaImagesResponse response = service.getTvSeasonEpisodeImage(id, season_number, episode_number, null, null);
                String[] parts = new String[] { MediaType.TV_EPISODE.name(), id + "", season_number + "", episode_number + "" };
                Media.Season.Episode episode = MediaResolver.resolveEpisode(mediaService, parts);

                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), response.getStills(), null,
                        null, null, episode, null, null, MediaType.TV_EPISODE);
            }
            default ->
            {
                System.out.println("not found type -> " + type);
                yield new ArrayList<>();
            }
        };
        //        if(!type.equals(MediaType.PERSON))
        //        {
        //            this.imageRepository.saveAll(images);
        //        }
        return images;
    }

    private List<Image> convertImageResponseToImages(final List<ImageResponse> backdrops, final List<ImageResponse> posters,
            final List<ImageResponse> logos, final List<ImageResponse> profiles, final List<ImageResponse> stills, final Media media,
            final Media.Season season, final Media.Season.Episode episode, final Person person, final Integer collectionId, final MediaType type)
    {
        List<Image> images = new ArrayList<>();

        if(backdrops != null)
        {
            for(ImageResponse backdrop : backdrops)
            {
                if(this.conditionInsert(type, backdrop))
                {
                    images.add(createImageObject(backdrop, media, season, episode, person, collectionId, ImageType.BACKDROP, type));
                }
            }
        }

        if(posters != null)
        {
            for(ImageResponse poster : posters)
            {
                if(this.conditionInsert(type, poster))
                {
                    images.add(createImageObject(poster, media, season, episode, person, collectionId, ImageType.POSTER, type));
                }
            }
        }

        if(logos != null)
        {
            for(ImageResponse logo : logos)
            {
                if(this.conditionInsert(type, logo))
                {
                    images.add(createImageObject(logo, media, season, episode, person, collectionId, ImageType.LOGO, type));
                }
            }
        }

        if(profiles != null)
        {
            for(ImageResponse profile : profiles)
            {
                if(this.conditionInsert(type, profile))
                {
                    images.add(createImageObject(profile, media, season, episode, person, collectionId, ImageType.PROFILE, type));
                }
            }
        }

        if(stills != null)
        {
            for(ImageResponse still : stills)
            {
                if(this.conditionInsert(type, still))
                {
                    images.add(createImageObject(still, media, season, episode, person, collectionId, ImageType.STILL, type));
                }
            }
        }

        return images;
    }

    private Image createImageObject(final ImageResponse image, final Media media, final Media.Season season, final Media.Season.Episode episode,
            final Person person, final Integer collection, final ImageType imageType, final MediaType type)
    {
        String cinevo_id = Shared.generateCinevoId(this.imageRepository.findByTmdbIdAndTypeAndFilePath(media != null
                        ? media.getTmdb_id()
                        : season != null ? season.getTmdb_id() : episode != null ? episode.getTmdb_id() : person != null ? person.getTmdb_id() : null,
                type.name(), image.getFile_path()));
        this.imageRepository.updateOrInsert(cinevo_id, type.name(), image.getAspect_ratio(), image.getHeight(), image.getIso_3166_1(),
                image.getIso_639_1(), image.getFile_path(), image.getVote_average(), image.getVote_count(), image.getWidth(), imageType.name(),
                collection, Shared.idOf(media), Shared.idOf(season), Shared.idOf(episode), Shared.idOf(person));

        return this.imageRepository.findByCinevoId(cinevo_id).get();
    }

    private Boolean conditionInsert(final MediaType type, final ImageResponse imageResponse)
    {
        return type == MediaType.PERSON || type == MediaType.TV_EPISODE || type == MediaType.TV_SEASON || (imageResponse.getIso_639_1() != null
                && imageResponse.getIso_639_1().equals("US")) || (imageResponse.getIso_3166_1() != null && imageResponse.getIso_3166_1()
                .equals("US"));
        //        return true;
    }
}
