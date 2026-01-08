package com.cinovo.backend.DB.Util;

import java.time.LocalDate;

public class Shared
{
    public final static String REGEX = "//././/";

    public static String[] onSplitObject(Object obj)
    {
        String input = String.valueOf(obj);
        return input.split(REGEX.replace(".", "\\."));
    }

    public static String onStringEqualsWithNull(final String str)
    {
        return str.equals("null") ? null : str;
    }

    public static Integer onStringParseToInteger(final String str)
    {
        return Shared.onStringEqualsWithNull(str) == null ? null : Integer.parseInt(str);
    }

    public static LocalDate onStringParseDate(String date)
    {
        return (date != null && !date.isBlank()) ? LocalDate.parse(date) : null;
    }

    public static String idOf(BaseEntity entity) {
        return entity == null ? null : entity.getCinevo_id();
    }

}
