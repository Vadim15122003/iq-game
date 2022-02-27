package project.rew.iqgamequiz;

public class GivenReward {

    String title,image,neededPoints;

    public GivenReward(String title, String image, String neededPoints) {
        this.title = title;
        this.image = image;
        this.neededPoints = neededPoints;
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

    public String getNeededPoints() {
        return neededPoints;
    }

    public void setNeededPoints(String neededPoints) {
        this.neededPoints = neededPoints;
    }
}
