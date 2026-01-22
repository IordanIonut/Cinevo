package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TvSeasonDetailsResponse
{
    private String _id;
    private String air_date;
    private List<Episode> episodes;
    private String name;
    private List<Network> networks;
    private String overview;
    private Integer id;
    private String poster_path;
    private Integer season_number;
    private String vote_average;

    @Data
    @Getter
    @Setter
    public static class Episode
    {
        private String air_date;
        private Integer episode_number;
        private String episode_type;
        private Integer id;
        private String name;
        private String overview;
        private String production_code;
        private Integer runtime;
        private Integer season_number;
        private Integer show_id;
        private String still_path;
        private Double vote_average;
        private Integer vote_count;
        private List<MediaResponse> crew;
        private List<MediaResponse> guest_stars;
    }

    @Data
    @Getter
    @Setter
    public static class Network
    {
        private Integer id;
        private String logo_path;
        private String name;
        private String origin_country;
    }
}
