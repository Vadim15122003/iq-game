package project.rew.iqgamequiz.mainactivities.play.questions.items;

import android.os.Parcel;
import android.os.Parcelable;

public class NivelAtributes implements Parcelable {

    String answer_img_btn, answer_img_corect, answer_img_incorect, answers_txt_color, back_btn,
            cinzeci_img, cinzeci_price, cinzeci_selected, coins_per_q, corect_img, corect_price,
            corect_selected, dialog_exit_img, dialog_exit_no, dialog_exit_txt_color, dialog_exit_yes,
            dialog_restart_img, dialog_restart_no, dialog_restart_txt_color, dialog_restart_yes,
            double_change_img, double_change_price, double_change_selected, exit_btn, finish_bckg_color,
            finish_text_color, glory_per_q, incorect_permision, pause_btn, pause_dialog_img, question_bckg,
            question_color, restart_btn, resume_btn, swich_img, swich_price, swich_selected_img,
            options_txt_price_color;

    public NivelAtributes() {
    }

    public NivelAtributes(String answer_img_btn, String answer_img_corect, String answer_img_incorect,
                          String answers_txt_color, String back_btn, String cinzeci_img,
                          String cinzeci_price, String cinzeci_selected, String coins_per_q,
                          String corect_img, String corect_price, String corect_selected,
                          String dialog_exit_img, String dialog_exit_no, String dialog_exit_txt_color,
                          String dialog_exit_yes, String dialog_restart_img, String dialog_restart_no,
                          String dialog_restart_txt_color, String dialog_restart_yes,
                          String double_change_img, String double_change_price,
                          String double_change_selected, String exit_btn, String finish_bckg_color,
                          String finish_text_color, String glory_per_q, String incorect_permision,
                          String pause_btn, String pause_dialog_img, String question_bckg,
                          String question_color, String restart_btn, String resume_btn, String swich_img,
                          String swich_price, String swich_selected_img, String options_txt_price_color) {
        this.answer_img_btn = answer_img_btn;
        this.answer_img_corect = answer_img_corect;
        this.answer_img_incorect = answer_img_incorect;
        this.answers_txt_color = answers_txt_color;
        this.back_btn = back_btn;
        this.cinzeci_img = cinzeci_img;
        this.cinzeci_price = cinzeci_price;
        this.cinzeci_selected = cinzeci_selected;
        this.coins_per_q = coins_per_q;
        this.corect_img = corect_img;
        this.corect_price = corect_price;
        this.corect_selected = corect_selected;
        this.dialog_exit_img = dialog_exit_img;
        this.dialog_exit_no = dialog_exit_no;
        this.dialog_exit_txt_color = dialog_exit_txt_color;
        this.dialog_exit_yes = dialog_exit_yes;
        this.dialog_restart_img = dialog_restart_img;
        this.dialog_restart_no = dialog_restart_no;
        this.dialog_restart_txt_color = dialog_restart_txt_color;
        this.dialog_restart_yes = dialog_restart_yes;
        this.double_change_img = double_change_img;
        this.double_change_price = double_change_price;
        this.double_change_selected = double_change_selected;
        this.exit_btn = exit_btn;
        this.finish_bckg_color = finish_bckg_color;
        this.finish_text_color = finish_text_color;
        this.glory_per_q = glory_per_q;
        this.incorect_permision = incorect_permision;
        this.pause_btn = pause_btn;
        this.pause_dialog_img = pause_dialog_img;
        this.question_bckg = question_bckg;
        this.question_color = question_color;
        this.restart_btn = restart_btn;
        this.resume_btn = resume_btn;
        this.swich_img = swich_img;
        this.swich_price = swich_price;
        this.swich_selected_img = swich_selected_img;
        this.options_txt_price_color = options_txt_price_color;
    }

    protected NivelAtributes(Parcel in) {
        answer_img_btn = in.readString();
        answer_img_corect = in.readString();
        answer_img_incorect = in.readString();
        answers_txt_color = in.readString();
        back_btn = in.readString();
        cinzeci_img = in.readString();
        cinzeci_price = in.readString();
        cinzeci_selected = in.readString();
        coins_per_q = in.readString();
        corect_img = in.readString();
        corect_price = in.readString();
        corect_selected = in.readString();
        dialog_exit_img = in.readString();
        dialog_exit_no = in.readString();
        dialog_exit_txt_color = in.readString();
        dialog_exit_yes = in.readString();
        dialog_restart_img = in.readString();
        dialog_restart_no = in.readString();
        dialog_restart_txt_color = in.readString();
        dialog_restart_yes = in.readString();
        double_change_img = in.readString();
        double_change_price = in.readString();
        double_change_selected = in.readString();
        exit_btn = in.readString();
        finish_bckg_color = in.readString();
        finish_text_color = in.readString();
        glory_per_q = in.readString();
        incorect_permision = in.readString();
        pause_btn = in.readString();
        pause_dialog_img = in.readString();
        question_bckg = in.readString();
        question_color = in.readString();
        restart_btn = in.readString();
        resume_btn = in.readString();
        swich_img = in.readString();
        swich_price = in.readString();
        swich_selected_img = in.readString();
        options_txt_price_color = in.readString();
    }

    public static final Creator<NivelAtributes> CREATOR = new Creator<NivelAtributes>() {
        @Override
        public NivelAtributes createFromParcel(Parcel in) {
            return new NivelAtributes(in);
        }

        @Override
        public NivelAtributes[] newArray(int size) {
            return new NivelAtributes[size];
        }
    };

    public String getAnswer_img_btn() {
        return answer_img_btn;
    }

    public void setAnswer_img_btn(String answer_img_btn) {
        this.answer_img_btn = answer_img_btn;
    }

    public String getAnswer_img_corect() {
        return answer_img_corect;
    }

    public void setAnswer_img_corect(String answer_img_corect) {
        this.answer_img_corect = answer_img_corect;
    }

    public String getAnswer_img_incorect() {
        return answer_img_incorect;
    }

    public void setAnswer_img_incorect(String answer_img_incorect) {
        this.answer_img_incorect = answer_img_incorect;
    }

    public String getAnswers_txt_color() {
        return answers_txt_color;
    }

    public void setAnswers_txt_color(String answers_txt_color) {
        this.answers_txt_color = answers_txt_color;
    }

    public String getBack_btn() {
        return back_btn;
    }

    public void setBack_btn(String back_btn) {
        this.back_btn = back_btn;
    }

    public String getCinzeci_img() {
        return cinzeci_img;
    }

    public void setCinzeci_img(String cinzeci_img) {
        this.cinzeci_img = cinzeci_img;
    }

    public String getCinzeci_price() {
        return cinzeci_price;
    }

    public void setCinzeci_price(String cinzeci_price) {
        this.cinzeci_price = cinzeci_price;
    }

    public String getCinzeci_selected() {
        return cinzeci_selected;
    }

    public void setCinzeci_selected(String cinzeci_selected) {
        this.cinzeci_selected = cinzeci_selected;
    }

    public String getCoins_per_q() {
        return coins_per_q;
    }

    public void setCoins_per_q(String coins_per_q) {
        this.coins_per_q = coins_per_q;
    }

    public String getCorect_img() {
        return corect_img;
    }

    public void setCorect_img(String corect_img) {
        this.corect_img = corect_img;
    }

    public String getCorect_price() {
        return corect_price;
    }

    public void setCorect_price(String corect_price) {
        this.corect_price = corect_price;
    }

    public String getCorect_selected() {
        return corect_selected;
    }

    public void setCorect_selected(String corect_selected) {
        this.corect_selected = corect_selected;
    }

    public String getDialog_exit_img() {
        return dialog_exit_img;
    }

    public void setDialog_exit_img(String dialog_exit_img) {
        this.dialog_exit_img = dialog_exit_img;
    }

    public String getDialog_exit_no() {
        return dialog_exit_no;
    }

    public void setDialog_exit_no(String dialog_exit_no) {
        this.dialog_exit_no = dialog_exit_no;
    }

    public String getDialog_exit_txt_color() {
        return dialog_exit_txt_color;
    }

    public void setDialog_exit_txt_color(String dialog_exit_txt_color) {
        this.dialog_exit_txt_color = dialog_exit_txt_color;
    }

    public String getDialog_exit_yes() {
        return dialog_exit_yes;
    }

    public void setDialog_exit_yes(String dialog_exit_yes) {
        this.dialog_exit_yes = dialog_exit_yes;
    }

    public String getDialog_restart_img() {
        return dialog_restart_img;
    }

    public void setDialog_restart_img(String dialog_restart_img) {
        this.dialog_restart_img = dialog_restart_img;
    }

    public String getDialog_restart_no() {
        return dialog_restart_no;
    }

    public void setDialog_restart_no(String dialog_restart_no) {
        this.dialog_restart_no = dialog_restart_no;
    }

    public String getDialog_restart_txt_color() {
        return dialog_restart_txt_color;
    }

    public void setDialog_restart_txt_color(String dialog_restart_txt_color) {
        this.dialog_restart_txt_color = dialog_restart_txt_color;
    }

    public String getDialog_restart_yes() {
        return dialog_restart_yes;
    }

    public void setDialog_restart_yes(String dialog_restart_yes) {
        this.dialog_restart_yes = dialog_restart_yes;
    }

    public String getDouble_change_img() {
        return double_change_img;
    }

    public void setDouble_change_img(String double_change_img) {
        this.double_change_img = double_change_img;
    }

    public String getDouble_change_price() {
        return double_change_price;
    }

    public void setDouble_change_price(String double_change_price) {
        this.double_change_price = double_change_price;
    }

    public String getDouble_change_selected() {
        return double_change_selected;
    }

    public void setDouble_change_selected(String double_change_selected) {
        this.double_change_selected = double_change_selected;
    }

    public String getExit_btn() {
        return exit_btn;
    }

    public void setExit_btn(String exit_btn) {
        this.exit_btn = exit_btn;
    }

    public String getFinish_bckg_color() {
        return finish_bckg_color;
    }

    public void setFinish_bckg_color(String finish_bckg_color) {
        this.finish_bckg_color = finish_bckg_color;
    }

    public String getFinish_text_color() {
        return finish_text_color;
    }

    public void setFinish_text_color(String finish_text_color) {
        this.finish_text_color = finish_text_color;
    }

    public String getGlory_per_q() {
        return glory_per_q;
    }

    public void setGlory_per_q(String glory_per_q) {
        this.glory_per_q = glory_per_q;
    }

    public String getIncorect_permision() {
        return incorect_permision;
    }

    public void setIncorect_permision(String incorect_permision) {
        this.incorect_permision = incorect_permision;
    }

    public String getPause_btn() {
        return pause_btn;
    }

    public void setPause_btn(String pause_btn) {
        this.pause_btn = pause_btn;
    }

    public String getPause_dialog_img() {
        return pause_dialog_img;
    }

    public void setPause_dialog_img(String pause_dialog_img) {
        this.pause_dialog_img = pause_dialog_img;
    }

    public String getQuestion_bckg() {
        return question_bckg;
    }

    public void setQuestion_bckg(String question_bckg) {
        this.question_bckg = question_bckg;
    }

    public String getQuestion_color() {
        return question_color;
    }

    public void setQuestion_color(String question_color) {
        this.question_color = question_color;
    }

    public String getRestart_btn() {
        return restart_btn;
    }

    public void setRestart_btn(String restart_btn) {
        this.restart_btn = restart_btn;
    }

    public String getResume_btn() {
        return resume_btn;
    }

    public void setResume_btn(String resume_btn) {
        this.resume_btn = resume_btn;
    }

    public String getSwich_img() {
        return swich_img;
    }

    public void setSwich_img(String swich_img) {
        this.swich_img = swich_img;
    }

    public String getSwich_price() {
        return swich_price;
    }

    public void setSwich_price(String swich_price) {
        this.swich_price = swich_price;
    }

    public String getSwich_selected_img() {
        return swich_selected_img;
    }

    public void setSwich_selected_img(String swich_selected_img) {
        this.swich_selected_img = swich_selected_img;
    }

    public String getOptions_txt_price_color() {
        return options_txt_price_color;
    }

    public void setOptions_txt_price_color(String options_txt_price_color) {
        this.options_txt_price_color = options_txt_price_color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(answer_img_btn);
        parcel.writeString(answer_img_corect);
        parcel.writeString(answer_img_incorect);
        parcel.writeString(answers_txt_color);
        parcel.writeString(back_btn);
        parcel.writeString(cinzeci_img);
        parcel.writeString(cinzeci_price);
        parcel.writeString(cinzeci_selected);
        parcel.writeString(coins_per_q);
        parcel.writeString(corect_img);
        parcel.writeString(corect_price);
        parcel.writeString(corect_selected);
        parcel.writeString(dialog_exit_img);
        parcel.writeString(dialog_exit_no);
        parcel.writeString(dialog_exit_txt_color);
        parcel.writeString(dialog_exit_yes);
        parcel.writeString(dialog_restart_img);
        parcel.writeString(dialog_restart_no);
        parcel.writeString(dialog_restart_txt_color);
        parcel.writeString(dialog_restart_yes);
        parcel.writeString(double_change_img);
        parcel.writeString(double_change_price);
        parcel.writeString(double_change_selected);
        parcel.writeString(exit_btn);
        parcel.writeString(finish_bckg_color);
        parcel.writeString(finish_text_color);
        parcel.writeString(glory_per_q);
        parcel.writeString(incorect_permision);
        parcel.writeString(pause_btn);
        parcel.writeString(pause_dialog_img);
        parcel.writeString(question_bckg);
        parcel.writeString(question_color);
        parcel.writeString(restart_btn);
        parcel.writeString(resume_btn);
        parcel.writeString(swich_img);
        parcel.writeString(swich_price);
        parcel.writeString(swich_selected_img);
        parcel.writeString(options_txt_price_color);
    }
}
