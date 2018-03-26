package othello.com.example.bhavneetsingh.notes;


public class News {
    String image;
    String title,content,next_news;

    public News(String image, String title, String content, String next_news) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.next_news = next_news;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNext_news() {
        return next_news;
    }

    public void setNext_news(String next_news) {
        this.next_news = next_news;
    }
}
