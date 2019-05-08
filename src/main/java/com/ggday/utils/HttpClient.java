package com.ggday.utils;

import com.google.common.base.Preconditions;
import okhttp3.*;

import java.io.IOException;

public class HttpClient {
    public static final MediaType VDN_API_JSON = MediaType.parse("application/vnd.api+json; charset=utf-8");
    public static final MediaType OCTET_STREAM = MediaType.parse("application/octet-stream; charset=utf-8");
    public static final String TOKEN_URL = SysProperties.getProperty(SysProperties.HOST) + "/session/token";

    private final OkHttpClient client;

    public HttpClient(String login, String password) {
        client = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(login, password)).build();
    }

    public Response sendRequest(Request request) {
        try {
           return client.newCall(request).execute();
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
            Request requestToken = new Request.Builder().url(TOKEN_URL).build();
            Response responseToken = new OkHttpClient().newCall(requestToken).execute();
            Preconditions.checkArgument(responseToken.code() == 200, "Incorrect response token code: " + responseToken.code());
            String token = Preconditions.checkNotNull(responseToken.body(), "Body is null").string();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials)
                    .header("X-CSRF-Token", token)
                    .build();
            return chain.proceed(authenticatedRequest);
        }
    }
}
