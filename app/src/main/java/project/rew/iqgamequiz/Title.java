package project.rew.iqgamequiz;

public class Title {

    String id,title,logo,image,color;

    public Title() {
    }

    public Title(String id, String title, String logo, String image, String color) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.image = image;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
