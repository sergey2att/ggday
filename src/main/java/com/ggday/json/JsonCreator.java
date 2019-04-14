package com.ggday.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.util.List;

public abstract class JsonCreator<T> {

     protected abstract String createJson(T t);

    public JsonArray createArray(List<Pair<String, String>> params) {
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        params.forEach(c -> object.addProperty(c.getKey(), c.getValue()));
        array.add(object);
        return array;
    }

    public JsonArray createArray(List<Pair<String, String>> params, Pair<String, ? extends Integer> param2) {
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        params.forEach(c -> object.addProperty(c.getKey(), c.getValue()));
        object.addProperty(param2.getKey(), param2.getValue());
        array.add(object);
        return array;
    }

    public JsonArray createArray(Pair<String, String> param, Pair<String, ? extends Integer> param2) {
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty(param.getKey(), param.getValue());
        object.addProperty(param2.getKey(), param2.getValue());
        array.add(object);
        return array;
    }

    public JsonArray createArray(String key, String value) {
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        array.add(object);
        return array;
    }

    public JsonArray createArray(String key, int value) {
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        array.add(object);
        return array;
    }
}
