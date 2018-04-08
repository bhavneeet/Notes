package othello.com.example.bhavneetsingh.notes;

/**
 * Created by bhavneet singh on 03-Apr-18.
 */

public class Cast implements ShowDetail {
    String name,poster,link,description,character;
    public Cast(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPoster() {
        return poster;
    }

    @Override
    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
