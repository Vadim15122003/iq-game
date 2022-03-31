package project.rew.iqgamequiz.mainactivities.friends.friendprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.FriendsActivity;
import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.friends.friendprofile.adapters.SeeImageAdapter;
import project.rew.iqgamequiz.mainactivities.friends.friendprofile.adapters.SeeTitleAdapter;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.mainactivities.friends.personaltest.PersonalQuestionActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.profile.ProfileActivity;
import project.rew.iqgamequiz.mainactivities.profile.adapters.SelectImageAdapter;
import project.rew.iqgamequiz.mainactivities.profile.adapters.SelectTitleAdapter;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class FriendProfileActivity extends AppCompatActivity {

    Friend friend;
    ImageView profImg, titleImg, titleLogo;
    TextView userName, title;
    Button addFriend, deleteF;
    ImageView profImgSee, titleLogoSee;
    TextView titleSee;
    FirebaseFirestore fstore;
    DatabaseReference ref;
    List<String> imagesId = new ArrayList<>();
    List<ProfileImage> images = new ArrayList<>();
    List<String> titlesId = new ArrayList<>();
    List<Title> titles = new ArrayList<>();
    SeeImageAdapter adapter;
    SeeTitleAdapter titleAdapter;
    RecyclerView recyclerView, recyclerViewT;
    Dialog dialogProfileImage, dialogTitle, dDeleteFriend;
    CardView PICardView, TCardView;
    Window wDeleteFriend;
    ImageView yes, no;
    Button makets, personal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        friend = getIntent().getParcelableExtra("friend");

        fstore = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("RO");
        profImg = findViewById(R.id.profile_img);
        titleImg = findViewById(R.id.title_image);
        titleLogo = findViewById(R.id.title_logo);
        userName = findViewById(R.id.username);
        title = findViewById(R.id.title);
        addFriend = findViewById(R.id.addFriend);
        deleteF = findViewById(R.id.deleteFriend);
        profImgSee = findViewById(R.id.profile_img_see);
        titleLogoSee = findViewById(R.id.title_logo_see);
        titleSee = findViewById(R.id.title_see);
        TCardView = findViewById(R.id.cardview_see_title);
        PICardView = findViewById(R.id.cardView_see);
        makets = findViewById(R.id.makets);
        personal = findViewById(R.id.personal);

        dialogTitle = new Dialog(FriendProfileActivity.this, R.style.DialogTransparentBg);
        dialogTitle.setContentView(R.layout.dialog_select_title);
        dialogTitle.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTitle.setCancelable(true);
        WindowManager.LayoutParams lp = dialogTitle.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialogTitle.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogProfileImage = new Dialog(FriendProfileActivity.this, R.style.DialogTransparentBg);
        dialogProfileImage.setContentView(R.layout.dialog_select_profile_image);
        dialogProfileImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogProfileImage.setCancelable(true);
        WindowManager.LayoutParams lp1 = dialogProfileImage.getWindow().getAttributes();
        lp1.dimAmount = 0.7f;
        dialogProfileImage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dDeleteFriend = new Dialog(FriendProfileActivity.this, R.style.DialogTransparentBg);
        dDeleteFriend.setContentView(R.layout.dialog_delete_friend);
        dDeleteFriend.setCancelable(true);
        wDeleteFriend = dDeleteFriend.getWindow();
        WindowManager.LayoutParams lp_exit = wDeleteFriend.getAttributes();
        lp_exit.dimAmount = 0.8f;
        wDeleteFriend.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        wDeleteFriend.setGravity(Gravity.CENTER);
        wDeleteFriend.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wDeleteFriend.getAttributes().windowAnimations = R.style.DialogAnimation;
        yes = dDeleteFriend.findViewById(R.id.yes);
        no = dDeleteFriend.findViewById(R.id.no);

        recyclerView = dialogProfileImage.findViewById(R.id.recyclerview);
        recyclerViewT = dialogTitle.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerViewT.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewT.setHasFixedSize(true);

        PICardView.setOnClickListener(v -> {
            dialogProfileImage.show();
        });
        TCardView.setOnClickListener(v -> {
            dialogTitle.show();
        });

        personal.setOnClickListener(v -> {
            openActivity(PersonalQuestionActivity.class);
        });
        makets.setOnClickListener(v -> {

        });

        getFriendType();
        setFriendsFunctions();
        getImgsAndTitles();
    }

    private void getFriendType() {
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

    private void setFriendsFunctions() {
        if (friend != null) {
            if (friend.getUsername() != null)
                userName.setText(friend.getUsername());
            if (friend.getTitle() != null && friend.getTitle().getTitle() != null) {
                title.setText(friend.getTitle().getTitle());
                titleSee.setText(friend.getTitle().getTitle());
            }
            if (friend.getTitle() != null && friend.getTitle().getColor() != null) {
                title.setTextColor(Color.parseColor(friend.getTitle().getColor()));
                titleSee.setTextColor(Color.parseColor(friend.getTitle().getColor()));
            }
            if (friend.getProfileImage() != null && friend.getProfileImage().getImage() != null) {
                Picasso.get().load(friend.getProfileImage().getImage()).into(profImg);
                Picasso.get().load(friend.getProfileImage().getImage()).into(profImgSee);
            }
            if (friend.getTitle() != null && friend.getTitle().getLogo() != null) {
                Picasso.get().load(friend.getTitle().getLogo()).into(titleLogo);
                Picasso.get().load(friend.getTitle().getLogo()).into(titleLogoSee);
            }
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
                        dDeleteFriend.show();
                        no.setOnClickListener(view -> {
                            dDeleteFriend.cancel();
                        });
                        yes.setOnClickListener(view_yes -> {
                            dDeleteFriend.cancel();
                            FirebaseUtils.deleteFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Anonym);
                            recreate();
                        });
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

    private void getImgsAndTitles() {
        fstore.collection("users").document(friend.getEmail())
                .collection("images").document("images").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            imagesId = (List<String>) documentSnapshot.get("id");
                            for (String imageId : imagesId) {
                                ref.child("Images").child(imageId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        images.add(new ProfileImage(imageId, snapshot.getValue().toString()));
                                        adapter = new SeeImageAdapter(images);
                                        recyclerView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                });

        fstore.collection("users").document(friend.getEmail())
                .collection("titles").document("titles").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            titlesId = (List<String>) documentSnapshot.get("id");
                            for (String titleId : titlesId) {
                                Title currentTitle = new Title();
                                currentTitle.setId(titleId);
                                ref.child("Titles").child(titleId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            currentTitle.setTitle(snapshot.child("title").getValue().toString());
                                            currentTitle.setColor(snapshot.child("color").getValue().toString());
                                            currentTitle.setLogo(snapshot.child("logo").getValue().toString());
                                            if (snapshot.child("image").exists())
                                                currentTitle.setImage(snapshot.child("image").getValue().toString());
                                            else currentTitle.setImage(null);
                                            titles.add(currentTitle);
                                            titleAdapter = new SeeTitleAdapter(titles);
                                            recyclerViewT.setAdapter(titleAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(FriendProfileActivity.this, cls);
        intent.putExtra("email",friend.getEmail());
        intent.putExtra("friend",friend);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FriendProfileActivity.this, FriendsActivity.class);
        startActivity(intent);
    }
}