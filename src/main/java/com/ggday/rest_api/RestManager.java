package com.ggday.rest_api;

import com.ggday.Starter;
import com.ggday.rest_api.dto.DTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;

public class RestManager {
    private final JsonParser parser = new JsonParser();

    public <T extends DTO>T callService(String url, Class<T> type) {
        JsonObject res = read(url);
        return DTO.newDTO(res, type);
    }

    private JsonObject read(String url) {
            URL uri;
            InputStreamReader reader;
            try {
                uri = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                Authenticator.setDefault(Starter.basicAuthenticator);
                reader = new InputStreamReader(uri.openStream());
            } catch (IOException e) {
                throw new RuntimeException("Can't read data from: " + url);
            }
            Authenticator.setDefault(null);
            JsonElement rawRoot = parser.parse(new JsonReader(reader));
            JsonObject root = (JsonObject) rawRoot;
            return root.getAsJsonObject("data");
    }
}
