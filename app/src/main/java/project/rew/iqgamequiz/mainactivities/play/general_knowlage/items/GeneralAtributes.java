package project.rew.iqgamequiz.mainactivities.play.general_knowlage.items;

import android.os.Parcel;
import android.os.Parcelable;

public class GeneralAtributes implements Parcelable{

    String background_img, claimed_img, details_bckg, details_txt_color, dialog_details_bckg,
            dialog_locked_txt_color, dialog_lockeg_img, dialog_txt_color, locked_img, locked_ok_img,
            title_color, unclaimed_img;

    public GeneralAtributes() {
    }

    public GeneralAtributes(String background_img, String claimed_img, String details_bckg,
                            String details_txt_color, String dialog_details_bckg,
                            String dialog_locked_txt_color, String dialog_lockeg_img,
                            String dialog_txt_color, String locked_img, String locked_ok_img,
                            String title_color, String unclaimed_img) {
        this.background_img = background_img;
        this.claimed_img = claimed_img;
        this.details_bckg = details_bckg;
        this.details_txt_color = details_txt_color;
        this.dialog_details_bckg = dialog_details_bckg;
        this.dialog_locked_txt_color = dialog_locked_txt_color;
        this.dialog_lockeg_img = dialog_lockeg_img;
        this.dialog_txt_color = dialog_txt_color;
        this.locked_img = locked_img;
        this.locked_ok_img = locked_ok_img;
        this.title_color = title_color;
        this.unclaimed_img = unclaimed_img;
    }

    protected GeneralAtributes(Parcel in) {
        background_img = in.readString();
        claimed_img = in.readString();
        details_bckg = in.readString();
        details_txt_color = in.readString();
        dialog_details_bckg = in.readString();
        dialog_locked_txt_color = in.readString();
        dialog_lockeg_img = in.readString();
        dialog_txt_color = in.readString();
        locked_img = in.readString();
        locked_ok_img = in.readString();
        title_color = in.readString();
        unclaimed_img = in.readString();
    }

    public static final Creator<GeneralAtributes> CREATOR = new Creator<GeneralAtributes>() {
        @Override
        public GeneralAtributes createFromParcel(Parcel in) {
            return new GeneralAtributes(in);
        }

        @Override
        public GeneralAtributes[] newArray(int size) {
            return new GeneralAtributes[size];
        }
    };

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public String getClaimed_img() {
        return claimed_img;
    }

    public void setClaimed_img(String claimed_img) {
        this.claimed_img = claimed_img;
    }

    public String getDetails_bckg() {
        return details_bckg;
    }

    public void setDetails_bckg(String details_bckg) {
        this.details_bckg = details_bckg;
    }

    public String getDetails_txt_color() {
        return details_txt_color;
    }

    public void setDetails_txt_color(String details_txt_color) {
        this.details_txt_color = details_txt_color;
    }

    public String getDialog_details_bckg() {
        return dialog_details_bckg;
    }

    public void setDialog_details_bckg(String dialog_details_bckg) {
        this.dialog_details_bckg = dialog_details_bckg;
    }

    public String getDialog_locked_txt_color() {
        return dialog_locked_txt_color;
    }

    public void setDialog_locked_txt_color(String dialog_locked_txt_color) {
        this.dialog_locked_txt_color = dialog_locked_txt_color;
    }

    public String getDialog_lockeg_img() {
        return dialog_lockeg_img;
    }

    public void setDialog_lockeg_img(String dialog_lockeg_img) {
        this.dialog_lockeg_img = dialog_lockeg_img;
    }

    public String getDialog_txt_color() {
        return dialog_txt_color;
    }

    public void setDialog_txt_color(String dialog_txt_color) {
        this.dialog_txt_color = dialog_txt_color;
    }

    public String getLocked_img() {
        return locked_img;
    }

    public void setLocked_img(String locked_img) {
        this.locked_img = locked_img;
    }

    public String getLocked_ok_img() {
        return locked_ok_img;
    }

    public void setLocked_ok_img(String locked_ok_img) {
        this.locked_ok_img = locked_ok_img;
    }

    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    public String getUnclaimed_img() {
        return unclaimed_img;
    }

    public void setUnclaimed_img(String unclaimed_img) {
        this.unclaimed_img = unclaimed_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(background_img);
        parcel.writeString(claimed_img);
        parcel.writeString(details_bckg);
        parcel.writeString(details_txt_color);
        parcel.writeString(dialog_details_bckg);
        parcel.writeString(dialog_locked_txt_color);
        parcel.writeString(dialog_lockeg_img);
        parcel.writeString(dialog_txt_color);
        parcel.writeString(locked_img);
        parcel.writeString(locked_ok_img);
        parcel.writeString(title_color);
        parcel.writeString(unclaimed_img);
    }
}
