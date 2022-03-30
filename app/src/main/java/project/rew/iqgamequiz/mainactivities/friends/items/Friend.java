package project.rew.iqgamequiz.mainactivities.friends.items;

import android.os.Parcel;
import android.os.Parcelable;

import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class Friend implements Parcelable {

    String username, email;
    ProfileImage profileImage;
    Title title;
    FriendType friendType;

    public Friend(String username, String email, ProfileImage profileImage, Title title, FriendType friendType) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.title = title;
        this.friendType = friendType;
    }

    public Friend() {
    }

    protected Friend(Parcel in) {
        username = in.readString();
        email = in.readString();
        profileImage = in.readParcelable(ProfileImage.class.getClassLoader());
        title = in.readParcelable(Title.class.getClassLoader());
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public FriendType getFriendType() {
        return friendType;
    }

    public void setFriendType(FriendType friendType) {
        this.friendType = friendType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeParcelable(profileImage, i);
        parcel.writeParcelable(title, i);
    }
}
