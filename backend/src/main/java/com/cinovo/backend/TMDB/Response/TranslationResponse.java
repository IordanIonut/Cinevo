package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TranslationResponse {
    private Integer id;
    private List<Translate> translations;

    @Getter
    @Setter
    @Data
    public static class Translate {
        private String iso_3166_1;
        private String iso_639_1;
        private String name;
        private String english_name;
        private Data data;

        @Getter
        @Setter
        public static class Data {
            private String title;
            private String overview;
            private String homepage;
        }
    }
}
