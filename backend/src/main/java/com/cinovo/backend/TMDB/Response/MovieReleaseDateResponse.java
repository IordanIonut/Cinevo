package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class MovieReleaseDateResponse
{
    private Integer id;
    List<Results> results;

    @Data
    @Getter
    @Setter
    public static class Results
    {
        private String iso_3166_1;
        List<ReleaseDate> release_dates;

        @Data
        @Getter
        @Setter
        public static class ReleaseDate
        {
            private String certification;
            private List<String> descriptors;
            private String iso_639_1;
            private String note;
            private String release_date;
            private Integer type;
        }
    }
}
