package com.ggday.rest_api.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DTO {

    private static final Gson gson = new Gson();

    public static <T extends DTO> T newDTO(JsonObject data, Class<T> type) {
        return gson.fromJson(data, type);
    }
}
