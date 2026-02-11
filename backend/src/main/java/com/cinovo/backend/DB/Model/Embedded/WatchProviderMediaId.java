package com.cinovo.backend.DB.Model.Embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class WatchProviderMediaId implements Serializable
{
    private String watch_provider_id;
    private String media_id;
}
