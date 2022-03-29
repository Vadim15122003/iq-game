package project.rew.iqgamequiz.mainactivities.friends.items;

import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class Friend {

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
}
