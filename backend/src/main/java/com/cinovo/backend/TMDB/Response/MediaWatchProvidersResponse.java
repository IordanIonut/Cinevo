package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
public class MediaWatchProvidersResponse
{
    private String id;
    private HashMap<String, WatchProvider> results;

    @Data
    @Getter
    @Setter
    public static class WatchProvider
    {
        private String link;
        private List<Possibility> buy;
        private List<Possibility> rent;
        private List<Possibility> flatrate;
        private List<Possibility> ads;
        private List<Possibility> free;

        @Data
        @Getter
        @Setter
        public static class Possibility
        {
            private String logo_path;
            private Integer provider_id;
            private String provider_name;
            private Integer display_priority;

        }
    }
}