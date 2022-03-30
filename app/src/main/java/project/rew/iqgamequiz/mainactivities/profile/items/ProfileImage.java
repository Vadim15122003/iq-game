package project.rew.iqgamequiz.mainactivities.profile.items;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileImage implements Parcelable {
    String id,image;

    public ProfileImage(String id, String image) {
        this.id = id;
        this.image = image;
    }

    protected ProfileImage(Parcel in) {
        id = in.readString();
        image = in.readString();
    }

    public static final Creator<ProfileImage> CREATOR = new Creator<ProfileImage>() {
        @Override
        public ProfileImage createFromParcel(Parcel in) {
            return new ProfileImage(in);
        }

        @Override
        public ProfileImage[] newArray(int size) {
            return new ProfileImage[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(image);
    }
}
