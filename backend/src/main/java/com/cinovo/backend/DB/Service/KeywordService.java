package com.cinovo.backend.DB.Service;

import aj.org.objectweb.asm.TypeReference;
import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Repository.KeywordRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.KeywordsResponse;
import com.cinovo.backend.TMDB.Response.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KeywordService implements TMDBLogically<Object, Object>
{
    private final KeywordRepository keywordRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public KeywordService(KeywordRepository keywordRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.keywordRepository = keywordRepository;
        this.service = service;
    }

    public Keyword findByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<Keyword> keyword = this.keywordRepository.findByTmdbId(tmdb_id);
        if(keyword.isEmpty())
        {
            return (Keyword) this.onConvertTMDB(tmdb_id);
        }
        return keyword.get();
    }

    public List<Keyword> getKeywordUsingSearch(final String query, final Integer page) throws Exception
    {
        //TODO: Add query to my database to find by query
        return (List<Keyword>) this.onConvertTMDB(this.service.getSearchKeywordsResponse(query, page));
    }

    @Override
    public Object onConvertTMDB(Object input) throws Exception
    {
        if(input instanceof SearchResponse<?>)
        {
            SearchResponse<?> rawResponse = (SearchResponse<?>) input;
            ObjectMapper mapper = new ObjectMapper();

            List<Keyword> keywords = new ArrayList<>();
            for(Object obj : rawResponse.getResults())
            {
                KeywordsResponse keywordsResponse = mapper.convertValue(obj, KeywordsResponse.class);
                Keyword keyword = this.genereateKeyword(keywordsResponse);
                keywords.add(keyword);
            }
            return keywords;
        }
        else if(input instanceof Integer)
        {
            KeywordsResponse response = this.service.getKeywordDetails((Integer) input);
            return this.genereateKeyword(response);
        }
        throw new IllegalArgumentException("Input of type object not find: " + input);
    }

    @Transactional
    protected Keyword genereateKeyword(KeywordsResponse response)
    {
        this.keywordRepository.updateOrInsert(Shared.generateCinevoId(this.keywordRepository.findByTmdbId(response.getId())), response.getId(),
                response.getName());

        return this.keywordRepository.findByTmdbId(response.getId()).get();
    }
}
