package othello.com.example.bhavneetsingh.notes;

/**
 * Created by bhavneet singh on 03-Apr-18.
 */

public class Cast implements ShowDetail {
    String name,poster_path,link,description,character,credit_id,id;
    int gender;
    public Cast(String name) {
        this.name = name;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPoster() {
        return poster_path;
    }

    @Override
    public void setPoster(String poster) {
        this.poster_path = poster;
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
