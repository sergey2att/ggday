package com.ggday.content_type;

public class Article {
    private final String title;

    protected Article(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
