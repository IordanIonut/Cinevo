package com.cinovo.backend.DB.Model.Enum;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TimeWindow {
    DAY("day"), WEEK("week");

    private final String label;

    TimeWindow(String label)
    {
        this.label = label;
    }
}
