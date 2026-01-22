package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Collection;
import com.cinovo.backend.DB.Repository.CollectionRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.CollectionResponse;
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
    private final CollectionRepository collectionRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public CollectionService(CollectionRepository collectionRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.collectionRepository = collectionRepository;
        this.service = service;
    }

    public Collection findByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<Collection> detail = this.collectionRepository.findByTmdbId(tmdb_id);
        if(detail.isEmpty())
        {
            return (Collection) this.onConvertTMDB(tmdb_id);
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
        this.collectionRepository.updateOrInsert(Shared.generateCinevoId(this.collectionRepository.findByTmdbId(collection.getId())),
                collection.getId(), collection.getName(), collection.getOverview(), collection.getPoster_path(), collection.getBackdrop_path(),
                collection.getOriginal_language(), collection.getOriginal_name(), collection.getAdult());
        return this.findByTmdbId(collection.getId());
    }
}
