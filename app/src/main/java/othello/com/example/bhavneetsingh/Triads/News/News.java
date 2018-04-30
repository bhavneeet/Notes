package othello.com.example.bhavneetsingh.Triads.News;


public class News {
   public static final String URL="newslink";
    String title,content,image,author,date,url;
    public News(String title, String context) {
        this.title = title;
        this.content = context;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return content;
    }

    public void setContext(String context) {
        this.content = context;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
