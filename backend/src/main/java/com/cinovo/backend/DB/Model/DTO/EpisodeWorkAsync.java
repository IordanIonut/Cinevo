package com.cinovo.backend.DB.Model.DTO;

import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeWorkAsync
{
    private Integer episodeId;
    private List<MediaResponse> guestStars;
    private List<MediaResponse> crews;
}