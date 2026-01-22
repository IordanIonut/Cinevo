package com.cinovo.backend.DB.Model.Enum;

public enum FileType
{
    SVG(".svg"), PNG(".png");

    private final String label;

    FileType(String label)
    {
        this.label = label;
    }

    public static FileType fromLabel(String label)
    {
        if(label == null || label.isBlank())
        {
            return null;
        }
        for(FileType status : values())
        {
            if(status.label.equalsIgnoreCase(label))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + label);
    }
}
