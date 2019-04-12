package content_type;

import java.util.List;

public class ArticlePortfolio extends Article {
    private final List<ArticleListItem> items;

    public ArticlePortfolio(String title, List<ArticleListItem> items) {
        super(title);
        this.items = items;
    }

    public List<ArticleListItem> getItems() {
        return items;
    }
}
