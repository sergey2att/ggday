package content_type;


public class ArticleStructured extends Article {

    private final String primaryImage;
    private final String description;

    public ArticleStructured(String title, String primaryImage, String description) {
        super(title);
        this.primaryImage = primaryImage;
        this.description = description;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public String getDescription() {
        return description;
    }
}
