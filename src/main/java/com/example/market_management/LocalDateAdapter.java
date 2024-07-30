package com.example.market_management;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final JsonSerializer<LocalDate> SERIALIZER = (src, typeOfSrc, context) ->
            new JsonPrimitive(src.format(DATE_FORMATTER));

    public static final JsonDeserializer<LocalDate> DESERIALIZER = (json, typeOfT, context) -> {
        try {
            String jsonString = json.getAsString();
            if (jsonString.contains("T")) {
                // If the date contains time and timezone info, parse as LocalDateTime first
                LocalDateTime dateTime = LocalDateTime.parse(jsonString, DATE_TIME_FORMATTER);
                return dateTime.toLocalDate();
            } else {
                // Otherwise, parse as LocalDate
                return LocalDate.parse(jsonString, DATE_FORMATTER);
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    };
}
