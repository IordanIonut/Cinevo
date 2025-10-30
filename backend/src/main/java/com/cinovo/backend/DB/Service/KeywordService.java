package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Repository.KeywordRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.KeywordsDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeywordService implements TMDBLogically<Integer, Keyword> {
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public Keyword findKeywordById(final Integer id) throws Exception {
        Optional<Keyword> keyword = this.keywordRepository.findKeywordById(id);
        if (keyword.isEmpty()) {
            return this.onConvertTMDB(id);
        }
        return keyword.get();
    }

    @Override
    public Keyword onConvertTMDB(Integer id) throws Exception {
        KeywordsDetailResponse keywordsDetailResponse = this.service.getKeywordDetails(id);
        Keyword keyword = new Keyword();
        keyword.setId(keywordsDetailResponse.getId());
        keyword.setName(keywordsDetailResponse.getName());

        keywordRepository.save(keyword);
        return keyword;
    }
}
