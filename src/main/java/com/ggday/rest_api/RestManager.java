package com.ggday.rest_api;

import com.ggday.rest_api.dto.DTO;
import com.ggday.utils.SysProperties;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class RestManager {
    private final JsonParser parser = new JsonParser();
    private static final String ARTICLE_URL = "http://zsergeyu.bget.ru/jsonapi/node/article/a79b4842-fdc1-4ad9-8d11-6f4a0961c59c";


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
                Authenticator authenticator = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SysProperties.getProperty(SysProperties.SITE_LOGIN), SysProperties.getProperty(SysProperties.SITE_PASSWORD).toCharArray());
                    }
                };
                Authenticator.setDefault(authenticator);

                reader = new InputStreamReader(uri.openStream());
            } catch (IOException e) {
                throw new RuntimeException("Can't read data from: " + url);
            }
            JsonElement rawRoot = parser.parse(new JsonReader(reader));
            JsonObject root = (JsonObject) rawRoot;

            return root.getAsJsonObject("data");
    }
}
