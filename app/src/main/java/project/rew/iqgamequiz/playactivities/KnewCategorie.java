package project.rew.iqgamequiz.playactivities;

public class KnewCategorie {
    private String title;
    private long id;
    private String image;

    public KnewCategorie(String title, long id, String image) {
        this.title = title;
        this.id = id;
        this.image = image;
    }

    public KnewCategorie() {

    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
