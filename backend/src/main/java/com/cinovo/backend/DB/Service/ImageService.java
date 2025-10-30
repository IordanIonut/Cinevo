package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Repository.ImageRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.ImageType;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.CollectionImageResponse;
import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import com.cinovo.backend.TMDB.Response.MovieImagesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements TMDBLogically<Object, Object>
{
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public List<Image> findImageById(final Integer id, final Type type) throws Exception
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

        return onConvertTMDB(Integer.parseInt(numbers), Type.valueOf(letters));
    }

    private List<Image> onConvertTMDB(Integer id, Type type) throws Exception
    {
        List<Image> images = switch(type)
        {
            case Type.MOVIE ->
            {
                MovieImagesResponse response = service.getMovieImages(id);
                Movie movie = movieService.getMovieById(response.getId());
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), response.getLogos(), movie, null);
            }
            case Type.COLLECTION ->
            {
                CollectionImageResponse response = service.getImageCollection(id);
                Integer collection = null; // TODO: implement collection lookup
                yield convertImageResponseToImages(response.getBackdrops(), response.getPosters(), null, null, collection);
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
            Movie movie, Integer collectionId)
    {
        List<Image> images = new ArrayList<>();

        if(backdrops != null)
        {
            for(ImageResponse backdrop : backdrops)
            {
                images.add(createImageObject(backdrop, movie, collectionId, ImageType.BACKDROP));
            }
        }

        if(posters != null)
        {
            for(ImageResponse poster : posters)
            {
                images.add(createImageObject(poster, movie, collectionId, ImageType.POSTER));
            }
        }

        if(logos != null)
        {
            for(ImageResponse logo : logos)
            {
                images.add(createImageObject(logo, movie, collectionId, ImageType.LOGO));
            }
        }

        return images;
    }

    private Image createImageObject(ImageResponse image, Movie movie, Integer collection, ImageType type)
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
        img.setMovie(movie);
        img.setCollection_id(collection);
        img.setImage_type(type);

        return img;
    }
}
