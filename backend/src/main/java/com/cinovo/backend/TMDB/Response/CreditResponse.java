package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CreditResponse {
    private Integer id;
    private List<Cast> cast;
    private List<Crew> crew;

    @Data
    @Getter
    @Setter
    public static class Cast {
        private Boolean adult;
        private Integer gender;
        private Integer id;
        private String known_for_department;
        private String name;
        private String original_name;
        private Integer popularity;
        private String profile_path;
        private Integer cast_id;
        private String character;
        private String credit_id;
        private Integer order;
    }

    @Data
    @Getter
    @Setter
    public static class Crew {
        private Boolean adult;
        private Integer id;
        private Integer gender;
        private String known_for_department;
        private String name;
        private String original_name;
        private Integer popularity;
        private String profile_path;
        private String credit_id;
        private String department;
        private String job;
    }
}
