package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class NetworkAlternativeNameResponse
{
    private Integer id;
    private List<Name> results;

    @Data
    @Getter
    @Setter
    public static class Name
    {
        private String name;
        private String type;
    }
}
