package project.rew.iqgamequiz.mainactivities.play.nivels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.GeneralAtributes;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.SelectGeneralKnowlage;
import project.rew.iqgamequiz.mainactivities.play.nivels.adapters.NivelSelectSlideAdapter;
import project.rew.iqgamequiz.mainactivities.play.nivels.enums.RewardType;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.Nivel;
import project.rew.iqgamequiz.mainactivities.play.questions.items.NivelAtributes;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class NivelSelectActivity extends AppCompatActivity {

    DatabaseReference ref;
    String categorie, categorieId;
    ViewPager2 viewPager;
    NivelSelectSlideAdapter adapter;
    TextView coins, glory;
    ConstraintLayout constraintLayout;
    FirebaseFirestore fstore;
    GeneralAtributes generalAtributes;
    List<Nivel> nivels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.viewpager);
        Bundle bundle = getIntent().getBundleExtra("Bundles");
        categorie = bundle.getString("categorie");
        generalAtributes = bundle.getParcelable("general_atributes");
        ref = FirebaseDatabase.getInstance().getReference().child("RO").child("Categories").child(categorie);
        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        constraintLayout = findViewById(R.id.constraint);
        fstore = FirebaseFirestore.getInstance();

        loadBackground();
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
                    if (snapshot.child("nivels").child(currNivel.getId())
                            .child("nivelAtributes").exists()) {
                        NivelAtributes nivelAtributes = new NivelAtributes();
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("answer_img_btn").exists())
                            nivelAtributes.setAnswer_img_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("answer_img_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("answer_img_corect").exists())
                            nivelAtributes.setAnswer_img_corect(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("answer_img_corect").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("answer_img_incorect").exists())
                            nivelAtributes.setAnswer_img_incorect(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("answer_img_incorect").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("answers_txt_color").exists())
                            nivelAtributes.setAnswers_txt_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("answers_txt_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("back_btn").exists())
                            nivelAtributes.setBack_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("back_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("cinzeci_img").exists())
                            nivelAtributes.setCinzeci_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("cinzeci_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("cinzeci_price").exists())
                            nivelAtributes.setCinzeci_price(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("cinzeci_price").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("cinzeci_selected").exists())
                            nivelAtributes.setCinzeci_selected(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("cinzeci_selected").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("coins_per_q").exists())
                            nivelAtributes.setCoins_per_q(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("coins_per_q").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("corect_img").exists())
                            nivelAtributes.setCorect_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("corect_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("corect_price").exists())
                            nivelAtributes.setCorect_price(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("corect_price").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("corect_selected").exists())
                            nivelAtributes.setCorect_selected(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("corect_selected").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_exit_img").exists())
                            nivelAtributes.setDialog_exit_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_exit_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_exit_no").exists())
                            nivelAtributes.setDialog_exit_no(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_exit_no").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_exit_txt_color").exists())
                            nivelAtributes.setDialog_exit_txt_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_exit_txt_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_exit_yes").exists())
                            nivelAtributes.setDialog_exit_yes(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_exit_yes").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_restart_img").exists())
                            nivelAtributes.setDialog_restart_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_restart_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_restart_no").exists())
                            nivelAtributes.setDialog_restart_no(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_restart_no").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_restart_txt_color").exists())
                            nivelAtributes.setDialog_restart_txt_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_restart_txt_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("dialog_restart_yes").exists())
                            nivelAtributes.setDialog_restart_yes(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("dialog_restart_yes").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("double_change_img").exists())
                            nivelAtributes.setDouble_change_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("double_change_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("double_change_price").exists())
                            nivelAtributes.setDouble_change_price(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("double_change_price").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("double_change_selected").exists())
                            nivelAtributes.setDouble_change_selected(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("double_change_selected").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("exit_btn").exists())
                            nivelAtributes.setExit_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("exit_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("finish_bckg_color").exists())
                            nivelAtributes.setFinish_bckg_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("finish_bckg_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("finish_text_color").exists())
                            nivelAtributes.setFinish_text_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("finish_text_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("glory_per_q").exists())
                            nivelAtributes.setGlory_per_q(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("glory_per_q").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("incorect_permision").exists())
                            nivelAtributes.setIncorect_permision(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("incorect_permision").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("options_txt_price_color").exists())
                            nivelAtributes.setOptions_txt_price_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("options_txt_price_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("pause_btn").exists())
                            nivelAtributes.setPause_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("pause_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("pause_dialog_img").exists())
                            nivelAtributes.setPause_dialog_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("pause_dialog_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("question_bckg").exists())
                            nivelAtributes.setQuestion_bckg(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("question_bckg").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("question_color").exists())
                            nivelAtributes.setQuestion_color(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("question_color").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("restart_btn").exists())
                            nivelAtributes.setRestart_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("restart_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("resume_btn").exists())
                            nivelAtributes.setResume_btn(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("resume_btn").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("swich_img").exists())
                            nivelAtributes.setSwich_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("swich_img").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("swich_price").exists())
                            nivelAtributes.setSwich_price(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("swich_price").getValue().toString());
                        if (snapshot.child("nivels").child(currNivel.getId())
                                .child("nivelAtributes").child("swich_selected_img").exists())
                            nivelAtributes.setSwich_selected_img(snapshot.child("nivels").child(currNivel.getId())
                                    .child("nivelAtributes").child("swich_selected_img").getValue().toString());

                        currNivel.setNivelAtributes(nivelAtributes);
                    }

                    if (snapshot.child("id").exists()) {
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        fstore.collection("users").document(FirebaseUtils.email)
                                .collection("resolved").document(snapshot.child("id").getValue().toString())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!currNivel.getId().equals("0") && documentSnapshot.exists() && documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1)) != null) {
                                    currNivel.setCurent(String.valueOf(documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1))));
                                } else
                                    currNivel.setCurent("0");

                                if (documentSnapshot.exists() && documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()))) != null) {
                                    currNivel.setCurentOfThis(String.valueOf(documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId())))));
                                } else
                                    currNivel.setCurentOfThis("0");

                                adapter = new NivelSelectSlideAdapter(nivels, categorie, categorieId, NivelSelectActivity.this, generalAtributes);
                                viewPager.setAdapter(adapter);

                            }
                        });
                    }
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

    void loadBackground() {
        if (generalAtributes != null && generalAtributes.getBackground_img() != null)
            Picasso.get().load(generalAtributes.getBackground_img()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    constraintLayout.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    loadBackground();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
    }
}