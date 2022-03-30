package project.rew.iqgamequiz.mainactivities.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class FriendProfileActivity extends AppCompatActivity {

    Friend friend;
    ImageView profImg, titleImg, titleLogo;
    TextView userName, title;
    Button addFriend,deleteF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        friend = getIntent().getParcelableExtra("friend");
        getFriendType(friend);

        profImg = findViewById(R.id.profile_img);
        titleImg = findViewById(R.id.title_image);
        titleLogo = findViewById(R.id.title_logo);
        userName = findViewById(R.id.username);
        title = findViewById(R.id.title);
        addFriend = findViewById(R.id.addFriend);
        deleteF = findViewById(R.id.deleteFriend);

            if (friend != null) {
                if (friend.getUsername() != null)
                    userName.setText(friend.getUsername());
                if (friend.getTitle() != null && friend.getTitle().getTitle() != null)
                    title.setText(friend.getTitle().getTitle());
                if (friend.getTitle() != null && friend.getTitle().getColor() != null)
                    title.setTextColor(Color.parseColor(friend.getTitle().getColor()));
                if (friend.getProfileImage() != null && friend.getProfileImage().getImage() != null)
                    Picasso.get().load(friend.getProfileImage().getImage()).into(profImg);
                if (friend.getTitle() != null && friend.getTitle().getLogo() != null)
                    Picasso.get().load(friend.getTitle().getLogo()).into(titleLogo);
                if (friend.getTitle() != null && friend.getTitle().getImage() != null) {
                    titleImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(friend.getTitle().getImage()).into(titleImg);
                } else titleImg.setVisibility(View.GONE);
                if (friend.getFriendType() != null) {
                    if (friend.getFriendType() == FriendType.Anonym) {
                        deleteF.setVisibility(View.GONE);
                        addFriend.setText("Add");
                        addFriend.setBackgroundColor(Color.parseColor("#FF3700B3"));
                        addFriend.setOnClickListener(v -> {
                            FirebaseUtils.inviteFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Pending);
                            recreate();
                        });
                    } else if (friend.getFriendType() == FriendType.Friend) {
                        deleteF.setVisibility(View.VISIBLE);
                        addFriend.setText("Friend");
                        addFriend.setClickable(false);
                        addFriend.setBackgroundColor(Color.parseColor("#047731"));
                        deleteF.setText("Delete Friend");
                        deleteF.setBackgroundColor(Color.parseColor("#403E3E"));
                        deleteF.setOnClickListener(v -> {
                            FirebaseUtils.deleteFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Anonym);
                            recreate();
                        });
                    } else if (friend.getFriendType() == FriendType.Inviting) {
                        addFriend.setText("Accept");
                        addFriend.setBackgroundColor(Color.parseColor("#0D5FCC"));
                        addFriend.setOnClickListener(v -> {
                            FirebaseUtils.acceptFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Friend);
                            recreate();
                        });
                        deleteF.setVisibility(View.VISIBLE);
                        deleteF.setText("Decline");
                        deleteF.setBackgroundColor(Color.parseColor("#F82424"));
                        deleteF.setOnClickListener(v -> {
                            FirebaseUtils.declineFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Anonym);
                            recreate();
                        });
                    } else if (friend.getFriendType() == FriendType.Pending) {
                        deleteF.setVisibility(View.VISIBLE);
                        addFriend.setText("Pending");
                        addFriend.setBackgroundColor(Color.parseColor("#59605C"));
                        addFriend.setClickable(false);
                        deleteF.setText("Cancel inviting");
                        deleteF.setBackgroundColor(Color.parseColor("#403E3E"));
                        deleteF.setOnClickListener(v -> {
                            FirebaseUtils.cancelInvite(friend.getEmail());
                            friend.setFriendType(FriendType.Anonym);
                            recreate();
                        });
                    }
                }
            }
    }

    private void getFriendType(Friend friend) {
        friend.setFriendType(FriendType.Anonym);
        if (FirebaseUtils.pendingFriends != null)
            for (String pendingFriends : FirebaseUtils.pendingFriends) {
                if (pendingFriends.equals(friend.getEmail())) {
                    friend.setFriendType(FriendType.Pending);
                    break;
                }
            }
        if (FirebaseUtils.inviteFriends != null)
            for (String invitingFriends : FirebaseUtils.inviteFriends) {
                if (invitingFriends.equals(friend.getEmail())) {
                    friend.setFriendType(FriendType.Inviting);
                    break;
                }
            }
        if (FirebaseUtils.friendsEmails != null)
            for (String actualFriends : FirebaseUtils.friendsEmails) {
                if (actualFriends.equals(friend.getEmail())) {
                    friend.setFriendType(FriendType.Friend);
                    break;
                }
            }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FriendProfileActivity.this, FriendsActivity.class);
        startActivity(intent);
    }
}