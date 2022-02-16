package project.rew.iqgamequiz;

public class Answer {
    String answer;
    boolean isCorect;

    public Answer(String answer, boolean isCorect) {
        this.answer = answer;
        this.isCorect = isCorect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorect() {
        return isCorect;
    }

    public void setCorect(boolean corect) {
        isCorect = corect;
    }
}
