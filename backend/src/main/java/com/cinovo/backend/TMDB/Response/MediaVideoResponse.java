package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MediaVideoResponse
{
    private Integer id;
    private List<Video> results;

    @Data
    @Getter
    @Setter
    public static class Video{
        private String iso_639_1;
        private String iso_3166_1;
        private String name;
        private String key;
        private String site;
        private Integer size;
        private String type;
        private Boolean official;
        private String published_at;
        private String id;
    }
}
