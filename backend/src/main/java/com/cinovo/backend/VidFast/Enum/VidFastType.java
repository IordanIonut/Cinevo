package com.cinovo.backend.VidFast.Enum;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum VidFastType
{
    PRO("pro"), IN("in"), IO("io"), ME("me"), NET("net"), PM("pm"), XYZ("xyz");
    private String value;

    VidFastType(String value)
    {
        this.value = value;
    }

}
