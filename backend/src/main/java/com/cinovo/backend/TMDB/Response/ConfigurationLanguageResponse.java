package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ConfigurationLanguageResponse {
    private String iso_639_1;
    private String english_name;
    private String name;
}
