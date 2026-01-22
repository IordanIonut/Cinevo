package com.cinovo.backend.DB.Model.Enum;

public enum Gender
{
    NOT_SET(0, "Not set / not specified"), FEMALE(1, "Female"), MALE(2, "Male"), NON_BINARY(3, "Non-binary");

    private final Integer code;
    private final String label;

    Gender(Integer code, String label)
    {
        this.code = code;
        this.label = label;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getLabel()
    {
        return label;
    }

    public static Gender fromCode(Integer code)
    {
        for(Gender gender : values())
        {
            if(gender.code == code)
            {
                return gender;
            }
        }
        return NOT_SET;
    }
}