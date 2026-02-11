package com.cinovo.backend.DB.Util;

import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Shared
{
    public final static String REGEX = "//././/";
    public final static ObjectMapper mapper = new ObjectMapper();

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

    public static String idOf(BaseEntity entity)
    {
        return entity == null ? null : entity.getCinevo_id();
    }

    public static Object onObjectToJson(Object object) throws JsonProcessingException
    {
        return mapper.writeValueAsString(object);
    }

    public static <T extends BaseEntity> String generateCinevoId(Optional<T> existing)
    {
        return existing.map(BaseEntity::getCinevo_id).orElse(UUID.randomUUID().toString());
    }

}
