package project.rew.iqgamequiz;

import java.util.List;

public class Question {
    String question, image, id;
    List<Answer> ansewrs;

    public Question(String question, String image, String id, List<Answer> ansewrs) {
        this.question = question;
        this.image = image;
        this.id = id;
        this.ansewrs = ansewrs;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Answer> getAnsewrs() {
        return ansewrs;
    }

    public void setAnsewrs(List<Answer> ansewrs) {
        this.ansewrs = ansewrs;
    }
}
