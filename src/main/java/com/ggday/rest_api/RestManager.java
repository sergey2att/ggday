package com.ggday.rest_api;

import com.ggday.content_type.Article;
import com.ggday.content_type.ArticleList;
import com.ggday.content_type.ArticleListItem;
import com.ggday.content_type.ArticleStructured;
import com.ggday.rest_api.dto.*;
import com.ggday.utils.HttpClient;
import com.ggday.utils.SysProperties;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RestManager {

    private static final String HOST = SysProperties.getProperty(SysProperties.HOST);
    private final JsonParser parser = new JsonParser();
    private final HttpClient httpClient = new HttpClient("login", "password");
    private Gson gson = new Gson();

    public DrupalElementDTO postArticle(Article article) {
        AttributesDTO.Builder attributesDTOBuilder = new AttributesDTO.Builder().title(article.getTitle());
        if (article instanceof ArticleStructured) {
            attributesDTOBuilder.body(new TextAreaDTO(((ArticleStructured) article).getDescription(), "full_html"));
        }
        if (article instanceof ArticleList) {
            attributesDTOBuilder.body(new TextAreaDTO(((ArticleList) article).getDescription(), "full_html"));
        }
        DrupalElementDTO drupalElementDTO = new DrupalElementDTO(null, "node--article", attributesDTOBuilder.build(), null);
        return readResponse(callServicePost("/jsonapi/node/article/", drupalElementDTO), DrupalElementDTO.class);
    }

    public DrupalElementDTO postParagraph(ArticleListItem item, int parentId) {
        AttributesDTO attributesDTO = new AttributesDTO.Builder().parentType("node")
                .parentFieldName("field_list_item").paragraphTitle(item.getTitle())
                .paragraphBody(new TextAreaDTO(item.getDescription(), "full_html")).parentId(parentId).build();
        DrupalElementDTO drupalElementDTO = new DrupalElementDTO(null, "paragraph--list_item", attributesDTO, null);
        return readResponse(callServicePost("/jsonapi/paragraph/list_item", drupalElementDTO), DrupalElementDTO.class);
    }

    public DrupalElementDTO patchArticleListItems(List<DrupalElementDTO> items, String articleId) {
        RelationshipListItemDTO relationshipListItemDTO =
                new RelationshipListItemDTO(items.stream().map(v -> new DataDTO(v.getId(), "paragraph--list_item", new MetaDTO(null, null, v.getAttributes().getDrupalInternalRevisionId()))).toArray(DataDTO[]::new));
        return readResponse(callServicePatchArticleListItem("/jsonapi/node/article/" + articleId, relationshipListItemDTO, articleId), DrupalElementDTO.class);
    }

    public DrupalElementDTO postPrimaryImage(String url, String articleId) {
        return postImage(url, HOST + "/jsonapi/node/article/" + articleId + "/field_image");
    }

    public DrupalElementDTO postParagraphImage(String url, String paragraphId) {
        return postImage(url, HOST + "/jsonapi/paragraph/list_item/" + paragraphId + "/field_image");
    }

    public DrupalElementDTO getArticle(String id) {
        return readResponse(callServiceGet("/jsonapi/node/article/" + id), DrupalElementDTO.class);
    }

    private DrupalElementDTO postImage(String imgUrl, String postPath) {
        byte[] data;
        try {
            data = encodeToString(imgUrl);
        } catch (IOException e) {
            throw new RuntimeException("Can't encode image");
        }
        RequestBody body = RequestBody.create(HttpClient.OCTET_STREAM, data);
        Request request = new Request.Builder()
                .header("Content-Type", "application/octet-stream")
                .header("Accept", "application/vnd.api+json")
                .header("Content-Disposition", "file; filename=\"" + generateName(imgUrl) + "\"")
                .url(postPath)
                .method("POST", body)
                .build();
        return readResponse(callServicePost(request), DrupalElementDTO.class);
    }

    private Response callServiceGet(String path) {
        Request req = buildGet(path);
        return httpClient.sendRequest(req);
    }

    private Response callServicePost(String path, Object object) {
        Request req = buildPost(path, object);
        return httpClient.sendRequest(req);
    }

    private Response callServicePatchArticleListItem(String path, Object object, String articleId) {
        Request req = buildArticleListItemPatch(path, object, articleId);
        return httpClient.sendRequest(req);
    }

    private Response callServicePost(Request request) {
        return httpClient.sendRequest(request);
    }

    private Request buildPost(String path, Object item) {
        JsonObject data = new JsonObject();
        data.add("data", gson.toJsonTree(item));
        RequestBody body = RequestBody.create(HttpClient.VDN_API_JSON, gson.toJson(data));
        return new Request.Builder()
                .url(HOST + path)
                .method("POST", body)
                .build();
    }

    //todo should be refactored
    private Request buildArticleListItemPatch(String path, Object item, String articleId) {
        JsonObject result = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject listItem = new JsonObject();
        data.addProperty("type", "node--article");
        data.addProperty("id", articleId);

        listItem.add("field_list_item", gson.toJsonTree(item).getAsJsonObject());
        data.add("relationships", listItem);
        result.add("data", data);
        RequestBody body = RequestBody.create(HttpClient.VDN_API_JSON, gson.toJson(result));
        return new Request.Builder()
                .url(HOST + path)
                .method("PATCH", body)
                .build();
    }

    private Request buildGet(String path) {
        return new Request.Builder()
                .url(HOST + path)
                .method("GET", null)
                .build();
    }

    private <T extends DTO> T readResponse(Response response, Class<T> type) {
        ResponseBody body = Preconditions.checkNotNull(response.body(), "Response body is null");
        JsonElement rawRoot = parser.parse(new JsonReader(new InputStreamReader(body.byteStream())));
        JsonObject root = (JsonObject) rawRoot;
        return DTO.newDTO(root.getAsJsonObject("data"), type);
    }

    private byte[] encodeToString(String imageURL) throws IOException {
        URL url = new java.net.URL(imageURL);
        InputStream is = url.openStream();
        return org.apache.commons.io.IOUtils.toByteArray(is);
    }

    private String generateName(String url) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddmmssSSS");
        return String.join(".", dateFormat.format(new Date()), getExt(url));
    }

    private String getExt(String url) {
        return Iterables.getLast(Arrays.asList(url.split("\\.")));
    }
}
