package com.cinovo.backend.TMDB.Response.Common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenresResponse {
    private List<Genre> genres;

    @Getter
    @Setter
    public static class Genre {
        private Integer id;
        private String name;
    }
}
