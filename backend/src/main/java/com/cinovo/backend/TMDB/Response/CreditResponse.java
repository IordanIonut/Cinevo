package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse
{
    private Integer id;
    private List<MediaResponse> crew;
    private List<MediaResponse> cast;
}
