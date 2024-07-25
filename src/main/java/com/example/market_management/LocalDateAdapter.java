package com.example.market_management;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter {

    public static final JsonSerializer<LocalDate> SERIALIZER = (src, typeOfSrc, context) ->
            new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));

    public static final JsonDeserializer<LocalDate> DESERIALIZER = (json, typeOfT, context) -> {
        try {
            return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    };
}
