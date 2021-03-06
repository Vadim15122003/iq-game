package project.rew.iqgamequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import project.rew.iqgamequiz.account.LoginActivity;
import project.rew.iqgamequiz.mainactivities.friends.FriendsActivity;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.mainactivities.profile.ProfileActivity;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.mainactivities.settings.Settings;
import project.rew.iqgamequiz.mainactivities.topglory.TopGlory;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.SelectGeneralKnowlage;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.utils.FirebaseUtils;

import static project.rew.iqgamequiz.account.LoginActivity.mAuth;

public class MainActivity extends AppCompatActivity {

    TextView coins, glory, username, title;
    ImageView profile_img, play, profile, friends, top_glory, settings, title_logo, title_image;
    TextView newsNrs;
    ImageView newNews;
    FirebaseFirestore fstore;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fstore = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() == null)
            sendUserToLoginActivity();
        ref = FirebaseDatabase.getInstance().getReference().child("RO");

        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        username = findViewById(R.id.username);
        title = findViewById(R.id.title);
        profile_img = findViewById(R.id.profile_img);
        play = findViewById(R.id.play);
        profile = findViewById(R.id.profile);
        friends = findViewById(R.id.friends);
        top_glory = findViewById(R.id.top_glory);
        settings = findViewById(R.id.settings);
        title_logo = findViewById(R.id.title_logo);
        title_image = findViewById(R.id.title_image);
        newNews = findViewById(R.id.newNews);
        newsNrs = findViewById(R.id.newsNrs);

        getData();
        getFriendsDetails();

        play.setOnClickListener(view -> openActivity(SelectGeneralKnowlage.class));
        profile.setOnClickListener(view -> openActivity(ProfileActivity.class));
        friends.setOnClickListener(view -> openActivity(FriendsActivity.class));
        top_glory.setOnClickListener(view -> openActivity(TopGlory.class));
        settings.setOnClickListener(view -> openActivity(Settings.class));
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getData() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseUtils.email = mAuth.getCurrentUser().getEmail();
            DocumentReference documentReference = fstore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()));
            documentReference.addSnapshotListener((value, error) -> {
                if (value != null) {
                    if (value.exists()) {
                        FirebaseUtils.coins = Objects.requireNonNull(value.get("IqCoins")).toString() + " ";
                        FirebaseUtils.glory = Objects.requireNonNull(value.get("glory")).toString() + " ";
                        FirebaseUtils.username = Objects.requireNonNull(value.get("username")).toString();
                        coins.setText(FirebaseUtils.coins);
                        glory.setText(FirebaseUtils.glory);
                        username.setText(FirebaseUtils.username);
                    }
                }
            });

            fstore.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .collection("images").document("selected").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                ref.child("Images").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            FirebaseUtils.profileImage = new ProfileImage(documentSnapshot.get("id").toString(), snapshot.getValue().toString());
                                            Picasso.get().load(FirebaseUtils.profileImage.getImage()).into(profile_img);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });

            fstore.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .collection("titles").document("selected").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                FirebaseUtils.title.setId(documentSnapshot.get("id").toString());
                                ref.child("Titles").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            FirebaseUtils.title.setTitle(snapshot.child("title").getValue().toString());
                                            title.setText(FirebaseUtils.title.getTitle());
                                            FirebaseUtils.title.setColor(snapshot.child("color").getValue().toString());
                                            title.setTextColor(Color.parseColor(FirebaseUtils.title.getColor()));
                                            FirebaseUtils.title.setLogo(snapshot.child("logo").getValue().toString());
                                            Picasso.get().load(FirebaseUtils.title.getLogo()).into(title_logo);
                                            if (snapshot.child("image").exists()) {
                                                FirebaseUtils.title.setImage(snapshot.child("image").getValue().toString());
                                                title_image.setVisibility(View.VISIBLE);
                                                Picasso.get().load(FirebaseUtils.title.getImage()).into(title_image);
                                            } else title_image.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });

        }
    }

    private void getFriendsDetails() {
        fstore.collection("users").document(FirebaseUtils.email)
                .collection("friends").document("friends").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            FirebaseUtils.friendsEmails = (List<String>) documentSnapshot.get("actual_friends");
                            FirebaseUtils.inviteFriends = (List<String>) documentSnapshot.get("inviting");
                            FirebaseUtils.pendingFriends = (List<String>) documentSnapshot.get("pending");
                            if (documentSnapshot.get("newFriends") != null)
                                FirebaseUtils.newFriends = FirebaseUtils.getInt(documentSnapshot.get("newFriends").toString());
                            else {
                                FirebaseUtils.newFriends = 0;
                            }
                            if (FirebaseUtils.inviteFriends != null && FirebaseUtils.newFriends +
                                    FirebaseUtils.inviteFriends.size() != 0) {
                                newNews.setVisibility(View.VISIBLE);
                                newsNrs.setVisibility(View.VISIBLE);
                                newsNrs.setText(String.valueOf(FirebaseUtils.newFriends +
                                        FirebaseUtils.inviteFriends.size()));
                            } else {
                                newNews.setVisibility(View.GONE);
                                newsNrs.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}