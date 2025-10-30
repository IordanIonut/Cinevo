package com.cinovo.backend.TMDB.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class CertificationResponse {
    private HashMap<String, List<Certification>> certifications;

    @Getter
    @Setter
    public static class Certification {
        private String certification;
        private String meaning;
        private Integer order;
    }
}
