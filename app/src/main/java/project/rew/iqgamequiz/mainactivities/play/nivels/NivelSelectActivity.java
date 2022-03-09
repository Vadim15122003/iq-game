package project.rew.iqgamequiz.mainactivities.play.nivels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.SelectGeneralKnowlage;
import project.rew.iqgamequiz.mainactivities.play.questions.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.questions.RewardType;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class NivelSelectActivity extends AppCompatActivity {

    DatabaseReference ref;
    String categorie, categorieId;
    ViewPager2 viewPager;
    NivelSelectSlideAdapter adapter;
    TextView coins, glory;
    FirebaseFirestore fstore;
    List<Nivel> nivels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.viewpager);
        categorie = getIntent().getStringExtra("categorie");
        ref = FirebaseDatabase.getInstance().getReference().child("RO").child("Categories").child(categorie);
        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        fstore = FirebaseFirestore.getInstance();

        glory.setText(FirebaseUtils.glory);
        coins.setText(FirebaseUtils.coins);
        ref.child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    categorieId = task.getResult().getValue().toString();
                }
            }
        });

        ref.child("nivels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nivels.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        nivels.add(new Nivel(dss.getKey().toString()));
                    }
                    setNivels();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(130));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1 - Math.abs(position);
                page.setScaleY(0.8f + v * 0.2f);
            }
        });
        viewPager.setPageTransformer(transformer);


    }

    public void setNivels() {
        for (Nivel currNivel : nivels) {

            DocumentReference documentReference = fstore.collection("users").document(FirebaseUtils.email)
                    .collection("rewards_claimed").document(categorieId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<String> claimed = new ArrayList<>();
                        if (document.exists()) {
                            claimed = ((List<String>) document.get(currNivel.getId()));
                        }
                        List<String> finalClaimed = claimed;
                        ref.child("nivels").child(currNivel.getId())
                                .child("rewards").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    List<GivenReward> givenRewards = new ArrayList<>();
                                    for (DataSnapshot dss : snapshot.getChildren()) {
                                        GivenReward givenReward = new GivenReward();
                                        GivenReward givenReward2 = new GivenReward();
                                        givenReward.setPointsNedeed(dss.getKey().toString());
                                        givenReward.setId(dss.child("id").getValue().toString());
                                        if (dss.child("title").exists() && !dss.child("image").exists()) {
                                            givenReward.setRewardType(RewardType.Title);
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            reference.child("RO").child("Titles").child(dss.child("title").getValue().toString())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                Title currentTitle = new Title();
                                                                currentTitle.setId(dss.child("title").getValue().toString());
                                                                currentTitle.setColor(snapshot.child("color").getValue().toString());
                                                                currentTitle.setLogo(snapshot.child("logo").getValue().toString());
                                                                currentTitle.setTitle(snapshot.child("title").getValue().toString());
                                                                givenReward.setTitle(currentTitle);
                                                                if (finalClaimed != null)
                                                                    for (String idClaimed : finalClaimed) {
                                                                        if (givenReward.getId().equals(idClaimed)) {
                                                                            givenReward.setClaimed(true);
                                                                            break;
                                                                        }
                                                                    }
                                                                givenRewards.add(givenReward);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        } else if (dss.child("image").exists() && !dss.child("title").exists()) {
                                            givenReward.setRewardType(RewardType.ProfileImage);
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            reference.child("RO").child("Images").child(dss.child("image").getValue().toString())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                ProfileImage profileImage = new ProfileImage(snapshot.getKey().toString(),
                                                                        snapshot.getValue().toString());
                                                                givenReward.setProfileImage(profileImage);
                                                                if (finalClaimed != null)
                                                                for (String idClaimed : finalClaimed) {
                                                                    if (givenReward.getId().equals(idClaimed)) {
                                                                        givenReward.setClaimed(true);
                                                                        break;
                                                                    }
                                                                }
                                                                givenRewards.add(givenReward);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        } else if (dss.child("image").exists() && dss.child("title").exists()) {
                                            givenReward2.setPointsNedeed(dss.getKey().toString());
                                            givenReward2.setId(dss.child("id").getValue().toString());

                                            givenReward.setRewardType(RewardType.Title);
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            reference.child("RO").child("Titles").child(dss.child("title").getValue().toString())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                Title currentTitle = new Title();
                                                                currentTitle.setId(dss.child("title").getValue().toString());
                                                                currentTitle.setColor(snapshot.child("color").getValue().toString());
                                                                currentTitle.setLogo(snapshot.child("logo").getValue().toString());
                                                                currentTitle.setTitle(snapshot.child("title").getValue().toString());
                                                                givenReward.setTitle(currentTitle);
                                                                if (finalClaimed != null)
                                                                for (String idClaimed : finalClaimed) {
                                                                    if (givenReward.getId().equals(idClaimed)) {
                                                                        givenReward.setClaimed(true);
                                                                        givenReward2.setClaimed(true);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                            givenReward2.setRewardType(RewardType.ProfileImage);
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                            reference1.child("RO").child("Images").child(dss.child("image").getValue().toString())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                ProfileImage profileImage = new ProfileImage(snapshot.getKey().toString(),
                                                                        snapshot.getValue().toString());
                                                                givenReward2.setProfileImage(profileImage);
                                                                givenRewards.add(givenReward);
                                                                givenRewards.add(givenReward2);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }

                                    }
                                    currNivel.setGivenRewards(givenRewards);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("nivels").child(currNivel.getId())
                            .child("title").exists())
                        currNivel.setTitle(snapshot.child("nivels").child(currNivel.getId())
                                .child("title").getValue().toString());
                    if (snapshot.child("nivels").child(currNivel.getId())
                            .child("image").exists())
                        currNivel.setImage(snapshot.child("nivels").child(currNivel.getId())
                                .child("image").getValue().toString());
                    if (snapshot.child("nivels").child(currNivel.getId())
                            .child("atributes").child("toUnlock").exists())
                        currNivel.setNedeed(snapshot.child("nivels").child(currNivel.getId())
                                .child("atributes").child("toUnlock").getValue().toString());

                    if (snapshot.child("id").exists() && !currNivel.getId().equals("0")) {
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        fstore.collection("users").document(FirebaseUtils.email)
                                .collection("resolved").document(snapshot.child("id").getValue().toString())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists() && documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1)) != null) {
                                    currNivel.setCurent(String.valueOf(documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1))));
                                } else
                                    currNivel.setCurent("0");

                                adapter = new NivelSelectSlideAdapter(nivels, categorie, categorieId, NivelSelectActivity.this);
                                viewPager.setAdapter(adapter);

                            }
                        });
                    } else if (currNivel.getId().equals("0")) currNivel.setCurent("0");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NivelSelectActivity.this, SelectGeneralKnowlage.class);
        startActivity(intent);
    }
}