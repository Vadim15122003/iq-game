package project.rew.iqgamequiz.mainactivities.friends.personaltest.items;

import java.util.List;

import project.rew.iqgamequiz.mainactivities.play.questions.items.Answer;

public class PersonalQuestion {
    String question;
    List<Answer> ansewrs;

    public PersonalQuestion(String question, List<Answer> ansewrs) {
        this.question = question;
        this.ansewrs = ansewrs;
    }

    public PersonalQuestion() {
    }

    public int getCorectAnswerPosition(){
        for(int i=0;i<ansewrs.size();i++){
            if (ansewrs.get(i).isCorect()) return i;
        }
        return 0;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnsewrs() {
        return ansewrs;
    }

    public void setAnsewrs(List<Answer> ansewrs) {
        this.ansewrs = ansewrs;
    }
}
