package project.rew.iqgamequiz.mainactivities.play.nivels.items;

import java.util.List;

import project.rew.iqgamequiz.mainactivities.play.questions.items.NivelAtributes;

public class Nivel {
    String title, image, curent, nedeed, id, curentOfThis;
    List<GivenReward> givenRewards;
    NivelAtributes nivelAtributes;

    public Nivel(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurent() {
        return curent;
    }

    public void setCurent(String curent) {
        this.curent = curent;
    }

    public String getNedeed() {
        return nedeed;
    }

    public void setNedeed(String nedeed) {
        this.nedeed = nedeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GivenReward> getGivenRewards() {
        return givenRewards;
    }

    public void setGivenRewards(List<GivenReward> givenRewards) {
        this.givenRewards = givenRewards;
    }

    public NivelAtributes getNivelAtributes() {
        return nivelAtributes;
    }

    public void setNivelAtributes(NivelAtributes nivelAtributes) {
        this.nivelAtributes = nivelAtributes;
    }

    public String getCurentOfThis() {
        return curentOfThis;
    }

    public void setCurentOfThis(String curentOfThis) {
        this.curentOfThis = curentOfThis;
    }
}
