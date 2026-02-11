package com.cinovo.backend.DB.Model.Embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class WatchProviderSeasonId
{
    private String watch_provider_id;
    private String season_id;
}
