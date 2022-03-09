package project.rew.iqgamequiz.mainactivities.play.questions;

import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class GivenReward {

    String pointsNedeed,id;
    Title title;
    ProfileImage profileImage;
    RewardType rewardType;
    boolean claimed;

    public GivenReward() {
        this.claimed=false;
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

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public String getPointsNedeed() {
        return pointsNedeed;
    }

    public void setPointsNedeed(String pointsNedeed) {
        this.pointsNedeed = pointsNedeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

}
