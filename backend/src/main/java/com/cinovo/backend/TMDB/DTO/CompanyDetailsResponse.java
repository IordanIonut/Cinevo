package com.cinovo.backend.TMDB.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CompanyDetailsResponse {
    private  String description;
    private String headquarters;
    private String homepage;
    private Integer id;
    private String logo_path;
    private String name;
    private String origin_country;
    private CompanyDetailsResponse parent_company;
}
