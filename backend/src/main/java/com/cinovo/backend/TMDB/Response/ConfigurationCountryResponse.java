package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ConfigurationCountryResponse {
    private String iso_3166_1;
    private String english_name;
    private String native_name;
}
