package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ConfigurationTimezonesResponse {
    private String iso_3166_1;
    private List<String> zones;
}
