package project.rew.iqgamequiz.mainactivities.play.general_knowlage.items;

public class KnewCategorie {
    String title;
    String id;
    String image;
    GeneralAtributes general_atributes;

    public KnewCategorie(String title, String id, String image, GeneralAtributes general_atributes) {
        this.title = title;
        this.id = id;
        this.image = image;
        this.general_atributes = general_atributes;
    }

    public KnewCategorie(String title) {
        this.title = title;
    }

    public KnewCategorie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public GeneralAtributes getGeneral_atributes() {
        return general_atributes;
    }

    public void setGeneral_atributes(GeneralAtributes general_atributes) {
        this.general_atributes = general_atributes;
    }
}