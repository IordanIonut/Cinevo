package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.KeywordsResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class MovieKeywordResponse
{
    private Integer id;
    private List<KeywordsResponse> keywords;
}