package othello.com.example.bhavneetsingh.Triads.Movies;


public class Season implements ShowDetail{
    
    String name,poster_path,link,description,air_date;

    public Season(String name, String poster) {
        this.name = name;
        this.poster_path = poster;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster_path;
    }

    public void setPoster(String poster) {
        this.poster_path = poster;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }
}
