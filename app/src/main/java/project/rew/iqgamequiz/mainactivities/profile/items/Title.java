package project.rew.iqgamequiz.mainactivities.profile.items;

import android.os.Parcel;
import android.os.Parcelable;

public class Title implements Parcelable {

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

    protected Title(Parcel in) {
        id = in.readString();
        title = in.readString();
        logo = in.readString();
        image = in.readString();
        color = in.readString();
    }

    public static final Creator<Title> CREATOR = new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel in) {
            return new Title(in);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(logo);
        parcel.writeString(image);
        parcel.writeString(color);
    }
}
