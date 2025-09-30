package com.cinovo.backend.TMDB.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LanguageResponse {
    private String iso_639_1;
    private String english_name;
    private String name;
}
