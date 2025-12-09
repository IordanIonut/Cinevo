package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Collection;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Repository.CollectionRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.CollectionResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JBossLog
public class CollectionService implements TMDBLogically<Object, Object>
{
    private final CollectionRepository detailRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final MediaService movieService;

    public CollectionService(CollectionRepository collectionRepository, @Lazy MediaService movieService, com.cinovo.backend.TMDB.Service service)
    {
        this.detailRepository = collectionRepository;
        this.movieService = movieService;
        this.service = service;
    }

    public Collection findCollectionById(final Integer id) throws Exception
    {
        Optional<Collection> detail = this.detailRepository.findCollectionById(id);
        if(detail.isEmpty())
        {
            return (Collection) this.onConvertTMDB(id);
        }
        return detail.get();
    }

    public List<Collection> getCollectionUsingSearch(final String query, final Boolean include_adult, final String language, final Integer page,
            final String region) throws Exception
    {
        //TODO: Add query to my database to find by query
        return (List<Collection>) this.onConvertTMDB(this.service.getSearchCollectionResponse(query, include_adult, language, page, region));
    }

    @Override
    public Object onConvertTMDB(Object input) throws Exception
    {
        if(input instanceof SearchResponse<?>)
        {
            SearchResponse<?> rawResponse = (SearchResponse<?>) input;
            ObjectMapper mapper = new ObjectMapper();
            List<Collection> collections = new ArrayList<>();
            for(Object obj : rawResponse.getResults())
            {
                CollectionResponse collectionResponse = mapper.convertValue(obj, CollectionResponse.class);
                Collection collection = this.generateCollection(collectionResponse);
                collections.add(collection);
            }
            return collections;
        }
        else if(input instanceof Integer)
        {
            CollectionResponse response = this.service.getCollectionDetail((Integer) input);
            return this.generateCollection(response);
        }
        throw new IllegalArgumentException("Invalid input and instanceof object for: " + input);
    }

    @Transactional
    public Collection generateCollection(CollectionResponse collection) throws Exception
    {
        Collection detail = this.detailRepository.findCollectionById(collection.getId()).orElse(new Collection());
        detail.setId(collection.getId());
        detail.setName(collection.getName());
        detail.setOverview(collection.getOverview());
        detail.setPoster_path(collection.getPoster_path());
        detail.setBackdrop_path(collection.getBackdrop_path());
        detail.setOriginal_language(collection.getOriginal_language());
        detail.setOriginal_name(collection.getOriginal_name());
        detail.setAdult(collection.getAdult());
        this.detailRepository.save(detail);

        if(collection.getParts() != null)
        {
            List<Media> medias = new ArrayList<>();
            for(MediaResponse movieResponse : collection.getParts())
            {
                Media movie =
                        this.movieService.getMediaByIdAndType(movieResponse.getId(), MediaType.valueOf(movieResponse.getMedia_type().toUpperCase()));
                medias.add(movie);
            }
            detail.setMedias(medias);
        }

        this.detailRepository.save(detail);
        return detail;
    }
}
