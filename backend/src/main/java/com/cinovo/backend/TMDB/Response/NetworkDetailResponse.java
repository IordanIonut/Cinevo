package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NetworkDetailResponse
{
    private String headquarters;
    private String homepage;
    private Integer id;
    private String logo_path;
    private String name;
    private String origin_country;
}
