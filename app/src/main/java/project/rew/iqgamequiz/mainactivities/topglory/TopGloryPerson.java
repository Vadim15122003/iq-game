package project.rew.iqgamequiz.mainactivities.topglory;

import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class TopGloryPerson {
    String email, place, glory, userName;
    Title title;
    ProfileImage profileImage;

    public TopGloryPerson() {
    }

    public TopGloryPerson(String email, String place, String glory, String userName, Title title, ProfileImage profileImage) {
        this.email = email;
        this.place = place;
        this.glory = glory;
        this.userName = userName;
        this.title = title;
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getGlory() {
        return glory;
    }

    public void setGlory(String glory) {
        this.glory = glory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
