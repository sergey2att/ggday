package com.ggday.scraper.proxy;

import html.elements.HtmlRow;
import html.elements.HtmlTable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ggday.scraper.Scraper.DEFAULT_USER_AGENT;

public class FreeProxyScraper implements ProxyProvided {

    private static final String url = "https://free-proxy-list.net/";
    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public Collection<HttpProxy> provide() {
        Collection<HttpProxy> res = new ArrayList<>();
        Document document = getHtmlDocument();
        HtmlTable proxiesTable = new HtmlTable(document.getElementById("proxylisttable"));
        List<HtmlRow> rows = proxiesTable.getHtmlRows().stream().limit(10).collect(Collectors.toList());
        rows.forEach(r -> res.add(new HttpProxy(r.getCell(0).getValue(), Integer.valueOf(r.getCell(1).getValue()))));
        return res;
    }

    private Document getHtmlDocument() {
        Request request = new Request.Builder()
                .url(FreeProxyScraper.url)
                .header("User-Agent", DEFAULT_USER_AGENT)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() != 200 && response.code() != 404) {
                throw new IllegalStateException("Incorrect status code" + response.code());
            }
            return Jsoup.parse(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            throw new RuntimeException("Can't get url: " + FreeProxyScraper.url + e);
        }
    }
}
