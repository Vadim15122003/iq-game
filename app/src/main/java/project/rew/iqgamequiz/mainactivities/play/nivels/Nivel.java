package project.rew.iqgamequiz.mainactivities.play.nivels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import project.rew.iqgamequiz.mainactivities.play.questions.GivenReward;

public class Nivel {
    String title, image, curent, nedeed, id;
    List<GivenReward> givenRewards;

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
}
