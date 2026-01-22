package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CompanyResponse {
    private  String description;
    private String headquarters;
    private String homepage;
    private Integer id;
    private String logo_path;
    private String name;
    private String origin_country;
    private CompanyResponse parent_company;
}
