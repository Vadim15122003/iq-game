package project.rew.iqgamequiz.mainactivities.play.general_knowlage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

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
import java.util.List;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.adapters.SelectGeneralKnowlageAdapter;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.GeneralAtributes;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.KnewCategorie;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;
import project.rew.iqgamequiz.mainactivities.play.nivels.adapters.NivelSelectSlideAdapter;
import project.rew.iqgamequiz.mainactivities.play.nivels.enums.RewardType;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.Nivel;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;


public class SelectGeneralKnowlage extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    SelectGeneralKnowlageAdapter adapter;
    GeneralAtributes generalAtributes;
    List<KnewCategorie> categories=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_general_knowlage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recyclerview);
        generalAtributes = new GeneralAtributes();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RO").child("Categories");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        categories.add(new KnewCategorie(dss.getKey().toString()));
                    }
                    setCategories(SelectGeneralKnowlage.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setCategories(Context context) {
        for (KnewCategorie currKnewCategorie : categories) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(currKnewCategorie.getTitle()).child("id").exists())
                        currKnewCategorie.setId(snapshot.child(currKnewCategorie.getTitle()).child("id").getValue().toString());
                    if (snapshot.child(currKnewCategorie.getTitle()).child("image").exists())
                        currKnewCategorie.setImage(snapshot.child(currKnewCategorie.getTitle()).child("image").getValue().toString());
                    if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").exists()) {
                        GeneralAtributes generalAtributes=new GeneralAtributes();
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("background_img").exists())
                            generalAtributes.setBackground_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("background_img").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("claimed_img").exists())
                            generalAtributes.setClaimed_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("claimed_img").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("details_bckg").exists())
                            generalAtributes.setDetails_bckg(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("details_bckg").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("details_txt_color").exists())
                            generalAtributes.setDetails_txt_color(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("details_txt_color").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_details_bckg").exists())
                            generalAtributes.setDialog_details_bckg(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_details_bckg").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_locked_txt_color").exists())
                            generalAtributes.setDialog_locked_txt_color(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_locked_txt_color").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_lockeg_img").exists())
                            generalAtributes.setDialog_lockeg_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_lockeg_img").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_txt_color").exists())
                            generalAtributes.setDialog_txt_color(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("dialog_txt_color").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("locked_img").exists())
                            generalAtributes.setLocked_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("locked_img").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("locked_ok_img").exists())
                            generalAtributes.setLocked_ok_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("locked_ok_img").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("title_color").exists())
                            generalAtributes.setTitle_color(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("title_color").getValue().toString());
                        if (snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("unclaimed_img").exists())
                            generalAtributes.setUnclaimed_img(snapshot.child(currKnewCategorie.getTitle()).child("general_atributes").child("unclaimed_img").getValue().toString());
                        currKnewCategorie.setGeneral_atributes(generalAtributes);
                    }
                    adapter=new SelectGeneralKnowlageAdapter(categories,context);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectGeneralKnowlage.this, MainActivity.class);
        startActivity(intent);
    }
}