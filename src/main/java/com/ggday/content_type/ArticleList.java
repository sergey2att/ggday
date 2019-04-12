package com.ggday.content_type;

import java.util.List;

public class ArticleList extends Article {

    private final List<ArticleListItem> items;
    private final String primaryImage;
    private final String description;

    public ArticleList(String title, String primaryImage, String description, List<ArticleListItem> items) {
        super(title);
        this.items = items;
        this.primaryImage = primaryImage;
        this.description = description;
    }

    public List<ArticleListItem> getItems() {
        return items;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public String getDescription() {
        return description;
    }
}
