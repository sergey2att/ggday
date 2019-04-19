package com.ggday.scraper;

import com.ggday.scraper.proxy.FreeProxyScraper;
import com.ggday.scraper.proxy.HttpProxy;
import com.ggday.scraper.proxy.ProxyRotator;
import com.ggday.scraper.proxy.RotationStrategy;
import com.google.common.base.Preconditions;
import com.ggday.content_type.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scraper {

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0";
    private static final String tagMarker = "ʖ";

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final Document document;
    private final ProxyRotator proxyRotator;

    public Scraper(String url) {
        this.document = getHtmlDocument(url);
        this.proxyRotator = new ProxyRotator(new FreeProxyScraper(), RotationStrategy.REFRESH);
    }

    public Article parse() {
        Elements articles = document.body().getElementsByTag("article");
        Article res = null;
        if (articles.size() > 0) {
            PageType type = parseType(articles.first().attr("class"));
            if (type == PageType.ARTICLE_PORTFOLIO) {
                res = parseArticlePortfolio();
            }
            if (type == PageType.ARTICLE_LIST) {
                res = parseArticleList();
                String s = "";
            }
            if (type == PageType.ARTICLE_STRUCTURED) {
                res = parseArticleStructured();
            }

        }
        Preconditions.checkNotNull(res, "Can't find article");
        return res;
    }

    private ArticlePortfolio parseArticlePortfolio() {
        List<ArticleListItem> items = new ArrayList<>();
        String title = parseTitle();
        Elements itemWrapper = document.body().getElementsByAttributeValueContaining("class", "ordered-list--portfolioTemplate");
        if (itemWrapper.size() > 0) {
            itemWrapper.first().getElementsByTag("li").forEach(li -> {
                String subtitle = handleText(li.getElementsByClass("ordered-list__header").first());
                String image = parserImage(li.getElementsByClass("img-placeholder").first().getElementsByTag("img").first());
                String description = handleText(li.getElementsByAttributeValueContaining("id", "list-item__description_").first());
                items.add(new ArticleListItem(subtitle, image, description));
            });
        }
        return new ArticlePortfolio(title, items);
    }

    private ArticleList parseArticleList() {
        List<ArticleListItem> items = new ArrayList<>();
        String title = parseTitle();
        String primaryImage = parserImage(document.getElementById("primary-media_1-0").getElementsByClass("img-placeholder")
                .first().getElementsByTag("img").first());
        String headerText = handleText(document.getElementsByAttributeValueContaining("id", "article__header--portfolio").first());
        Elements itemWrapper = document.body().getElementsByAttributeValueContaining("id", "ordered-list--structured");
        if (itemWrapper.size() > 0) {
            itemWrapper.first().getElementsByTag("li").forEach(li -> {
                String subtitle = handleText(li.getElementsByClass("ordered-list__header").first());
                String image = parserImage(li.getElementsByClass("img-placeholder").first().getElementsByTag("img").first());
                String description = handleText(li.getElementsByClass("ordered-list__content-description").first());
                items.add(new ArticleListItem(subtitle, image, description));
            });
        }
        return new ArticleList(title, primaryImage, headerText, items);
    }

    private ArticleStructured parseArticleStructured() {
        String title = parseTitle();
        Element wrapper = Preconditions.checkNotNull(document.body().getElementsByClass("article__body").first());
        String primaryImage = parserImage(wrapper.getElementById("primary-media_1-0").getElementsByClass("img-placeholder")
                .first().getElementsByTag("img").first());
        String description = handleText(wrapper.getElementsByClass("chop-content").first());
        return new ArticleStructured(title, primaryImage, description);
    }

    private String handleText(Element textElement) {
        String description = Preconditions.checkNotNull(textElement, "Description is null").html();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        Whitelist whitelist = Whitelist.relaxed();
        whitelist.removeTags("div", "span", "a", "figure");
        description = Parser.unescapeEntities(Jsoup.clean(description, "", whitelist, settings), false);

        LinkedList<String> allMatches = new LinkedList<>();
        Matcher m = Pattern.compile("<.*?>").matcher(description);
        while (m.find()) {
            allMatches.add(m.group());
        }
        description = description.replaceAll("<.*?>", tagMarker);
        description = Stream.of(description.split(tagMarker, -1)).map(s -> {
            if (StringUtils.isBlank(s)) {
                return "";
            } else {
                return StringUtils.normalizeSpace(translateText(s));
            }
        }).collect(Collectors.joining(tagMarker))
                .replaceAll(tagMarker + tagMarker, tagMarker + "\n" + tagMarker);

        for (String allMatch : allMatches) {
            description = description.replaceFirst(tagMarker, allMatch);
        }
        return description;
    }

    private String parseTitle() {
        Element title = Preconditions.checkNotNull(document.body().getElementsByClass("heading__title").first(),
                "title is null");
        return handleText(title);
    }

    private String parserImage(Element img) {
        Preconditions.checkNotNull(img, "Image is null");
        if (img.attr("data-src").isEmpty()) {
            return img.attr("src");
        } else {
            return img.attr("data-src");
        }
    }

    private PageType parseType(String classAttribute) {
        if (classAttribute.contains("article--portfolio"))
            return PageType.ARTICLE_PORTFOLIO;
        if (classAttribute.contains("article--list"))
            return PageType.ARTICLE_LIST;
        if (classAttribute.contains("article--structured"))
            return PageType.ARTICLE_STRUCTURED;
        throw new IllegalArgumentException("Unable to detect article type");
    }

    private Document getHtmlDocument(String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", DEFAULT_USER_AGENT)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() != 200 && response.code() != 404) {
                throw new IllegalStateException("Incorrect status code" + response.code());
            }
            return Jsoup.parse(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            throw new RuntimeException("Can't get url: " + url + e);
        }
    }

    private String tryToGetWithProxy(String url) {
        while (true) {
            try {
                HttpProxy currentProxy = proxyRotator.rotate();
                System.out.println("proxyIp: " + currentProxy.getIp() + ", proxyPort: " + currentProxy.getPort());
                Connection.Response response = Jsoup.connect(url).proxy(currentProxy.getIp(), currentProxy.getPort())
                        .userAgent(DEFAULT_USER_AGENT).ignoreContentType(true)
                        .header("Content-Language", "ru-RU").execute();
                if (response.statusCode() != 200 && response.statusCode() != 404) {
                    System.out.println("Incorrect response status code: " + response.statusCode());
                }
                return  response.parse().text();

            } catch (IOException e) {
                System.out.println("Request fails: " + e.getMessage());
            }
        }
    }

    private String translateText(String text) {
        StringBuilder resultString = new StringBuilder();
        try {
            String googleTransUrl = "http://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=ru&dt=t&q=" +
                    URLEncoder.encode(text, "UTF-8") + "&ie=UTF-8&oe=UTF-8";
            String res = tryToGetWithProxy(googleTransUrl);
            Pattern p = Pattern.compile("\"([^\"]*)\",[^.*]");
            Matcher m = p.matcher(res);

            while (m.find()) {
                resultString.append(m.group(1));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't encode string: " + text);
        }
        return resultString.toString();
    }

    private Document getHtmlDocument(Connection connection) {
        try {
            Connection.Response response = connection.execute();
            if (response.statusCode() != 200 && response.statusCode() != 404) {
                throw new IllegalStateException("Incorrect status code" + response.statusCode());
            }
            return response.parse();
        } catch (IOException e) {
            throw new RuntimeException("Can't get url: " + connection.response().url().toString() + e);
        }
    }
}
