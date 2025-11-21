package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.KeywordsResponse;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class MediaKeywordResponse
{
    private Integer id;
    @JsonAlias({ "keywords", "results" })
    private List<KeywordsResponse> keywords;
}