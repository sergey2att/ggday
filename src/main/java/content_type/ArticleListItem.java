package content_type;

public class ArticleListItem {
    private final String title;
    private final String image;
    private final String description;

    public ArticleListItem(String title, String image, String description) {
        this.title = title;
        this.image = image;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
