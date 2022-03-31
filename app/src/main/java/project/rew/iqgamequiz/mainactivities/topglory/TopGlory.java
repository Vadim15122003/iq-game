package project.rew.iqgamequiz.mainactivities.topglory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.adapters.QuestionAdapter;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Answer;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Question;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

import static project.rew.iqgamequiz.account.LoginActivity.mAuth;

public class TopGlory extends AppCompatActivity {

    RecyclerView recyclerView;
    TopGloryAdapter adapter;
    List<TopGloryPerson> topGloryPeople=new ArrayList<>();
    FirebaseFirestore fstore;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_glory);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = findViewById(R.id.recyclerLay);
        fstore = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getData();
    }

    void getData(){
        ref.child("TopGlory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topGloryPeople.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        String currEmail=dss.getValue().toString();
                        TopGloryPerson currTopGloryPerson = new TopGloryPerson();
                        currTopGloryPerson.setEmail(currEmail);
                        currTopGloryPerson.setPlace(dss.getKey().toString());

                        fstore.collection("users").document(Objects.requireNonNull(currEmail)).addSnapshotListener((value, error) -> {
                            if (value != null) {
                                if (value.exists()) {
                                    currTopGloryPerson.setGlory(Objects.requireNonNull(value.get("glory")).toString());
                                    currTopGloryPerson.setUserName(Objects.requireNonNull(value.get("username")).toString());
                                }
                            }
                        });

                        fstore.collection("users").document(currEmail)
                                .collection("images").document("selected").get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            ref.child("RO").child("Images").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        currTopGloryPerson.setProfileImage(new ProfileImage(documentSnapshot.get("id").toString(),snapshot.getValue().toString()));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                });

                        fstore.collection("users").document(currEmail)
                                .collection("titles").document("selected").get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            Title title=new Title();
                                            title.setId(documentSnapshot.get("id").toString());
                                            ref.child("RO").child("Titles").child(documentSnapshot.get("id").toString()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        title.setTitle(snapshot.child("title").getValue().toString());
                                                        title.setColor(snapshot.child("color").getValue().toString());
                                                        title.setLogo(snapshot.child("logo").getValue().toString());
                                                        if (snapshot.child("image").exists()) {
                                                            title.setImage(snapshot.child("image").getValue().toString());
                                                        }
                                                        currTopGloryPerson.setTitle(title);
                                                    }

                                                    topGloryPeople.add(currTopGloryPerson);
                                                    adapter=new TopGloryAdapter(topGloryPeople);
                                                    recyclerView.setAdapter(adapter);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}