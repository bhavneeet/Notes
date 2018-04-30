package othello.com.example.bhavneetsingh.Triads.Movies;

public class Movie {
    public static final String URL="movie_url";
    String name,id,description,poster;
    String season_link,cast_link,video_link;


    public Movie(String name) {
        this.name = name;
    }

    public String getSeason_link() {
        return season_link;
    }

    public void setSeason_link(String season_link) {
        this.season_link = season_link;
    }

    public String getCast_link() {
        return cast_link;
    }

    public void setCast_link(String cast_link) {
        this.cast_link = cast_link;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return "http://image.tmdb.org/t/p/original/"+poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
}
