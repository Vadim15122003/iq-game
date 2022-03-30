package project.rew.iqgamequiz.mainactivities.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.adapters.FriendsAdapter;
import project.rew.iqgamequiz.mainactivities.friends.adapters.SearchFriendsAdapter;
import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.SelectGeneralKnowlage;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class FriendsActivity extends AppCompatActivity {

    EditText search;
    Button searchBtn;
    RecyclerView friendsRecyclerView, friendsInviteRV, searchRV, pendingsRv;
    FirebaseFirestore fstore;
    DatabaseReference ref;
    SearchFriendsAdapter adapterSearch, adapterInvites, adapterPendings;
    List<Friend> friendAdds = new ArrayList<>();
    List<Friend> friends = new ArrayList<>();
    List<Friend> friendsInvite = new ArrayList<>();
    List<Friend> friendsPending = new ArrayList<>();
    FriendsAdapter adapter;
    TextView friendInvTxt, friendsTxt, pendingsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        search = findViewById(R.id.searchFriend);
        searchBtn = findViewById(R.id.buttonSearch);
        friendsRecyclerView = findViewById(R.id.recyclerview);
        friendsInviteRV = findViewById(R.id.invites_rv);
        friendInvTxt = findViewById(R.id.friends_inivite_txt);
        friendsTxt = findViewById(R.id.friends_txt);
        pendingsTxt = findViewById(R.id.friends_pending_txt);
        pendingsRv = findViewById(R.id.pending_rv);
        searchRV = findViewById(R.id.searchRV);
        fstore = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("RO");

        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsInviteRV.setLayoutManager(new LinearLayoutManager(this));
        searchRV.setLayoutManager(new LinearLayoutManager(this));
        pendingsRv.setLayoutManager(new LinearLayoutManager(this));

        if (FirebaseUtils.newFriends != 0) FirebaseUtils.setNewFriendsZero();
        getFriends();
        getInvitedFriends();
        getPendingFriends();
        searchFriends();
    }

    private void getFriends() {
        friends.clear();
        if (FirebaseUtils.friendsEmails != null)
            for (String friendEmail : FirebaseUtils.friendsEmails) {

                Friend friend = new Friend();
                friend.setEmail(friendEmail);
                friend.setFriendType(FriendType.Friend);

                fstore.collection("users").document(friendEmail)
                        .addSnapshotListener((value, error) -> {
                            if (value != null) {
                                if (value.exists()) {
                                    friend.setUsername(Objects.requireNonNull(value.get("username")).toString());
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("images").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    ref.child("Images").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                friend.setProfileImage(new ProfileImage(documentSnapshot.get("id").toString(), snapshot.getValue().toString()));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("titles").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Title title = new Title();
                                    title.setId(documentSnapshot.get("id").toString());
                                    ref.child("Titles").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                title.setTitle(snapshot.child("title").getValue().toString());
                                                title.setColor(snapshot.child("color").getValue().toString());
                                                title.setLogo(snapshot.child("logo").getValue().toString());
                                                if (snapshot.child("image").exists()) {
                                                    title.setImage(snapshot.child("image").getValue().toString());
                                                }
                                                friend.setTitle(title);
                                                friends.add(friend);
                                                adapter = new FriendsAdapter(friends,FriendsActivity.this);
                                                friendsRecyclerView.setAdapter(adapter);
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

    private void getInvitedFriends() {
        friendsInvite.clear();
        if (FirebaseUtils.inviteFriends != null && FirebaseUtils.inviteFriends.size() != 0) {
            friendInvTxt.setVisibility(View.VISIBLE);
            friendsInviteRV.setVisibility(View.VISIBLE);
            for (String friendEmail : FirebaseUtils.inviteFriends) {

                Friend friend = new Friend();
                friend.setEmail(friendEmail);
                friend.setFriendType(FriendType.Inviting);

                fstore.collection("users").document(friendEmail)
                        .addSnapshotListener((value, error) -> {
                            if (value != null) {
                                if (value.exists()) {
                                    friend.setUsername(Objects.requireNonNull(value.get("username")).toString());
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("images").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    ref.child("Images").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                friend.setProfileImage(new ProfileImage(documentSnapshot.get("id").toString(), snapshot.getValue().toString()));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("titles").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Title title = new Title();
                                    title.setId(documentSnapshot.get("id").toString());
                                    ref.child("Titles").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                title.setTitle(snapshot.child("title").getValue().toString());
                                                title.setColor(snapshot.child("color").getValue().toString());
                                                title.setLogo(snapshot.child("logo").getValue().toString());
                                                if (snapshot.child("image").exists()) {
                                                    title.setImage(snapshot.child("image").getValue().toString());
                                                }
                                                friend.setTitle(title);
                                                friendsInvite.add(friend);
                                                adapterInvites = new SearchFriendsAdapter(friendsInvite, FriendsActivity.this);
                                                friendsInviteRV.setAdapter(adapterInvites);
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
        } else {
            friendInvTxt.setVisibility(View.GONE);
            friendsInviteRV.setVisibility(View.GONE);
        }
    }

    private void getPendingFriends() {
        friendsPending.clear();
        if (FirebaseUtils.pendingFriends != null && FirebaseUtils.pendingFriends.size() != 0) {
            pendingsRv.setVisibility(View.VISIBLE);
            pendingsTxt.setVisibility(View.VISIBLE);
            for (String friendEmail : FirebaseUtils.pendingFriends) {

                Friend friend = new Friend();
                friend.setEmail(friendEmail);
                friend.setFriendType(FriendType.Pending);

                fstore.collection("users").document(friendEmail)
                        .addSnapshotListener((value, error) -> {
                            if (value != null) {
                                if (value.exists()) {
                                    friend.setUsername(Objects.requireNonNull(value.get("username")).toString());
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("images").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    ref.child("Images").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                friend.setProfileImage(new ProfileImage(documentSnapshot.get("id").toString(), snapshot.getValue().toString()));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });

                fstore.collection("users").document(friendEmail)
                        .collection("titles").document("selected").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Title title = new Title();
                                    title.setId(documentSnapshot.get("id").toString());
                                    ref.child("Titles").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                title.setTitle(snapshot.child("title").getValue().toString());
                                                title.setColor(snapshot.child("color").getValue().toString());
                                                title.setLogo(snapshot.child("logo").getValue().toString());
                                                if (snapshot.child("image").exists()) {
                                                    title.setImage(snapshot.child("image").getValue().toString());
                                                }
                                                friend.setTitle(title);
                                                friendsPending.add(friend);
                                                adapterPendings = new SearchFriendsAdapter(friendsPending, FriendsActivity.this);
                                                pendingsRv.setAdapter(adapterPendings);
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
        } else {
            pendingsRv.setVisibility(View.GONE);
            pendingsTxt.setVisibility(View.GONE);
        }
    }

    private void searchFriends() {
        searchBtn.setOnClickListener(v -> {
            String usernameForSearch = search.getText().toString();
            friendAdds.clear();
            if (!usernameForSearch.equals("")) {
                friendsInviteRV.setVisibility(View.GONE);
                friendInvTxt.setVisibility(View.GONE);
                friendsTxt.setVisibility(View.GONE);
                friendsRecyclerView.setVisibility(View.GONE);
                pendingsRv.setVisibility(View.GONE);
                pendingsTxt.setVisibility(View.GONE);
                searchRV.setVisibility(View.VISIBLE);
                fstore.collection("users").get().
                        addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    if (!snapshot.getId().toString().equals(FirebaseUtils.email)
                                            && snapshot.get("username").toString().toLowerCase()
                                            .contains(usernameForSearch.toLowerCase())) {
                                        Friend friendAdd = new Friend();
                                        friendAdd.setEmail(snapshot.getId().toString());
                                        friendAdd.setFriendType(FriendType.Anonym);
                                        if (FirebaseUtils.pendingFriends != null)
                                            for (String pendingFriends : FirebaseUtils.pendingFriends) {
                                                if (pendingFriends.equals(friendAdd.getEmail())) {
                                                    friendAdd.setFriendType(FriendType.Pending);
                                                    break;
                                                }
                                            }
                                        if (FirebaseUtils.inviteFriends != null)
                                            for (String invitingFriends : FirebaseUtils.inviteFriends) {
                                                if (invitingFriends.equals(friendAdd.getEmail())) {
                                                    friendAdd.setFriendType(FriendType.Inviting);
                                                    break;
                                                }
                                            }
                                        if (FirebaseUtils.friendsEmails != null)
                                            for (String actualFriends : FirebaseUtils.friendsEmails) {
                                                if (actualFriends.equals(friendAdd.getEmail())) {
                                                    friendAdd.setFriendType(FriendType.Friend);
                                                    break;
                                                }
                                            }
                                        friendAdd.setUsername(snapshot.get("username").toString());
                                        fstore.collection("users").document(friendAdd.getEmail())
                                                .collection("titles").document("selected").get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            Title title = new Title();
                                                            title.setId(documentSnapshot.get("id").toString());
                                                            ref.child("Titles").child(title.getId()).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) {
                                                                        title.setTitle(snapshot.child("title").getValue().toString());
                                                                        title.setColor(snapshot.child("color").getValue().toString());
                                                                        title.setLogo(snapshot.child("logo").getValue().toString());
                                                                        if (snapshot.child("image").exists()) {
                                                                            title.setImage(snapshot.child("image").getValue().toString());
                                                                        }
                                                                        friendAdd.setTitle(title);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                        fstore.collection("users").document(friendAdd.getEmail()).
                                                collection("images").document("selected").
                                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                ref.child("Images").child(documentSnapshot.get("id").toString())
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    friendAdd.setProfileImage(new ProfileImage(documentSnapshot.get("id").toString(), snapshot.getValue().toString()));
                                                                    friendAdds.add(friendAdd);
                                                                    adapterSearch = new SearchFriendsAdapter(friendAdds, FriendsActivity.this);
                                                                    searchRV.setAdapter(adapterSearch);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                            }
                                        });
                                    }
                                }
                            }
                        });
                adapterSearch = new SearchFriendsAdapter(friendAdds, FriendsActivity.this);
                searchRV.setAdapter(adapterSearch);
            } else {
                searchRV.setVisibility(View.GONE);
                if (FirebaseUtils.inviteFriends != null && FirebaseUtils.inviteFriends.size() != 0) {
                    friendsInviteRV.setVisibility(View.VISIBLE);
                    friendInvTxt.setVisibility(View.VISIBLE);
                }
                if (FirebaseUtils.pendingFriends != null && FirebaseUtils.pendingFriends.size() != 0) {
                    pendingsRv.setVisibility(View.VISIBLE);
                    pendingsTxt.setVisibility(View.VISIBLE);
                }
                friendsTxt.setVisibility(View.VISIBLE);
                friendsRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (friendsRecyclerView.getVisibility() == View.GONE) {
            searchRV.setVisibility(View.GONE);
            if (FirebaseUtils.inviteFriends != null && FirebaseUtils.inviteFriends.size() != 0) {
                friendsInviteRV.setVisibility(View.VISIBLE);
                friendInvTxt.setVisibility(View.VISIBLE);
            }
            if (FirebaseUtils.pendingFriends != null && FirebaseUtils.pendingFriends.size() != 0) {
                pendingsRv.setVisibility(View.VISIBLE);
                pendingsTxt.setVisibility(View.VISIBLE);
            }
            friendsTxt.setVisibility(View.VISIBLE);
            friendsRecyclerView.setVisibility(View.VISIBLE);
            getFriends();
            getInvitedFriends();
            getPendingFriends();
        } else {
            Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}