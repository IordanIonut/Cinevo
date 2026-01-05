package com.cinovo.backend.VidFast.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result
{
    private String url;
    private int httpStatus;
    private String finalUrl;
    private boolean tlsOk;
    private boolean frameAllowed;
    private Map<String, String> frameHeaders;
    private boolean usable;
    private String error;
}
