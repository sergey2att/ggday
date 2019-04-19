package com.ggday.utils;

import okhttp3.*;

import java.io.IOException;

public class HttpClient {
    public static final MediaType JSON = MediaType.parse("application/rest_api; charset=utf-8");
    public static final MediaType HAL_JSON = MediaType.parse("application/hal+rest_api; charset=utf-8");
    public static final String TOKEN_URL = "http://zsergeyu.bget.ru/session/token";



    private final OkHttpClient client;

    public HttpClient() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("login", "password")).build();
    }

    public void sendRequest(String url, String json, String method, MediaType type) {
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder()
                .url(url)
                .method(method, body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class BasicAuthInterceptor implements Interceptor {
        private String credentials;

        BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials)
                    .build();
            return chain.proceed(authenticatedRequest);
        }
    }
}
