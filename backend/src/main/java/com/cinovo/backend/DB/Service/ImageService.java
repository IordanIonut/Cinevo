package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.ImageRepository;
import com.cinovo.backend.DB.Util.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.ImageType;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.CollectionImageResponse;
import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaImagesResponse;
import com.cinovo.backend.TMDB.Response.PeopleImagesResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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

    public List<Image> findImageByMediaIdAndMediaType(final Integer id, final MediaType type) throws Exception
    {
        Optional<List<Image>> image = this.imageRepository.findImageByMediaIdAndMediaType(id, type.name());
        if(image.isEmpty() || image.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(type + Shared.REGEX + id + Shared.REGEX + null + Shared.REGEX + null);
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

    public List<Image> findImageBySeasonIdAndSeasonNumberAndEpisodeAndMediaType(final Integer series_id, final Integer season_number,
            final Integer episode_number, final MediaType type) throws Exception
    {
        Optional<List<Image>> images =
                this.imageRepository.findImageBySeasonIdAndSeasonNumberAndEpisodeAndMediaType(series_id, season_number, episode_number);
        if(images.isEmpty() || images.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(type + Shared.REGEX + series_id + Shared.REGEX + season_number + Shared.REGEX + episode_number);
        }
        return images.get();
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
                Media media = mediaService.getMediaByIdAndType(response.getId(), MediaType.MOVIE);
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), null, null, media, null, null,
                        null, null, MediaType.MOVIE);
            }
            case MediaType.PERSON ->
            {
                PeopleImagesResponse response = service.getPeopleImages(id);
                Person person = this.personService.findPersonById(id);
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
                Media media = mediaService.getMediaByIdAndType(response.getId(), MediaType.TV);
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

        this.imageRepository.saveAll(images);
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
                images.add(createImageObject(backdrop, media, season, episode, person, collectionId, ImageType.BACKDROP, type));
            }
        }

        if(posters != null)
        {
            for(ImageResponse poster : posters)
            {
                images.add(createImageObject(poster, media, season, episode, person, collectionId, ImageType.POSTER, type));
            }
        }

        if(logos != null)
        {
            for(ImageResponse logo : logos)
            {
                images.add(createImageObject(logo, media, season, episode, person, collectionId, ImageType.LOGO, type));
            }
        }

        if(profiles != null)
        {
            for(ImageResponse profile : profiles)
            {
                images.add(createImageObject(profile, media, season, episode, person, collectionId, ImageType.PROFILE, type));
            }
        }

        if(stills != null)
        {
            for(ImageResponse still : stills)
            {
                images.add(createImageObject(still, media, season, episode, person, collectionId, ImageType.STILL, type));
            }
        }

        return images;
    }

    private Image createImageObject(final ImageResponse image, final Media media, final Media.Season season, final Media.Season.Episode episode,
            final Person person, final Integer collection, final ImageType imageType, final MediaType type)
    {
        Image img =
                this.imageRepository.findByMediaIdAndCollectionIdAndImageTypeAndType(media != null ? media.getCinevo_id() : null, imageType.name(),
                        collection, type.name()).orElse(new Image());
        img.setAspect_ratio(image.getAspect_ratio());
        img.setHeight(image.getHeight());
        img.setWidth(image.getWidth());
        img.setIso_3166_1(image.getIso_3166_1());
        img.setFile_path(image.getFile_path());
        img.setIso_639_1(image.getIso_639_1());
        img.setVote_average(image.getVote_average());
        img.setVote_count(image.getVote_count());
        img.setMedia(media);
        img.setCollection_id(collection);
        img.setImage_type(imageType);
        img.setType(type);
        img.setSeason(season);
        img.setPerson(person);
        img.setEpisode(episode);

        return img;
    }
}
