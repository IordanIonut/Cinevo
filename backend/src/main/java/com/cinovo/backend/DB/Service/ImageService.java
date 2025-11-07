package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Repository.ImageRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.ImageType;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.CollectionImageResponse;
import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import com.cinovo.backend.TMDB.Response.MovieImagesResponse;
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
    private final MediaService movieService;
    private final com.cinovo.backend.TMDB.Service service;

    public ImageService(ImageRepository imageRepository, @Lazy MediaService movieService, com.cinovo.backend.TMDB.Service service)
    {
        this.imageRepository = imageRepository;
        this.movieService = movieService;
        this.service = service;
    }

    public List<Image> findImageById(final Integer id, final MediaType type) throws Exception
    {
        Optional<List<Image>> image = this.imageRepository.findImageByIdAndType(id, type.name());
        if(image.isEmpty() || image.get().isEmpty())
        {
            return (List<Image>) this.onConvertTMDB(id + "" + type);
        }
        return image.get();
    }

    @Override
    public Object onConvertTMDB(Object input) throws Exception
    {
        String inputStr = String.valueOf(input);
        String numbers = inputStr.replaceAll("[^0-9]", "");
        String letters = inputStr.replaceAll("[^A-Za-z]", "");

        return onConvertTMDB(Integer.parseInt(numbers), MediaType.valueOf(letters));
    }

    private List<Image> onConvertTMDB(Integer id, MediaType type) throws Exception
    {
        List<Image> images = switch(type)
        {
            case MediaType.MOVIE ->
            {
                MovieImagesResponse response = service.getMovieImages(id);
                Media media = movieService.getMediaByIdAndType(response.getId(), MediaType.MOVIE);
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), null, media, null,
                        MediaType.MOVIE);
            }
            case MediaType.PERSON ->
            {
                PeopleImagesResponse response = service.getPeopleImages(id);
                yield convertImageResponseToImages(null, null, null, response.getProfiles(), null, null, MediaType.PERSON);
            }
            case MediaType.COLLECTION ->
            {
                CollectionImageResponse response = service.getImageCollection(id);
                Integer collection = null; // TODO: implement collection lookup
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), null, null, null, collection, MediaType.COLLECTION);
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

    private List<Image> convertImageResponseToImages(List<ImageResponse> backdrops, List<ImageResponse> posters, List<ImageResponse> logos,
            List<ImageResponse> profiles, Media media, Integer collectionId, MediaType type)
    {
        List<Image> images = new ArrayList<>();

        if(backdrops != null)
        {
            for(ImageResponse backdrop : backdrops)
            {
                images.add(createImageObject(backdrop, media, collectionId, ImageType.BACKDROP, type));
            }
        }

        if(posters != null)
        {
            for(ImageResponse poster : posters)
            {
                images.add(createImageObject(poster, media, collectionId, ImageType.POSTER, type));
            }
        }

        if(logos != null)
        {
            for(ImageResponse logo : logos)
            {
                images.add(createImageObject(logo, media, collectionId, ImageType.LOGO, type));
            }
        }

        if(profiles != null)
        {
            for(ImageResponse profile : profiles)
            {
                images.add(createImageObject(profile, media, collectionId, ImageType.PROFILE, type));
            }
        }

        return images;
    }

    private Image createImageObject(final ImageResponse image, final Media media, final Integer collection, final ImageType imageType,
            final MediaType type)
    {
        Image img = new Image();
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
        return img;
    }
}
