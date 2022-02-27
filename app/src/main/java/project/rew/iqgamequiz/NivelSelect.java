package project.rew.iqgamequiz;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.mainactivities.SelectGeneralKnowlage;
import project.rew.iqgamequiz.utils.FirebaseUtils;
import project.rew.iqgamequiz.utils.NivelSelectSlideAdapter;

public class NivelSelect extends AppCompatActivity {

    DatabaseReference ref;
    String categorie, categorieId;
    ViewPager2 viewPager;
    NivelSelectSlideAdapter adapter;
    TextView coins, glory;
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
            ref.child("nivels").child(currNivel.getId()).child("title").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        currNivel.setTitle(String.valueOf(task.getResult().getValue()));
                    }
                }
            });
            ref.child("nivels").child(currNivel.getId()).child("image").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        currNivel.setImage(String.valueOf(task.getResult().getValue()));
                    }
                }
            });
            ref.child("nivels").child(currNivel.getId()).child("atributes").child("toUnlock").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        currNivel.setNedeed(String.valueOf(task.getResult().getValue()));
                    }
                }
            });
            ref.child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful() && !currNivel.getId().equals("0")) {
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        fstore.collection("users").document(FirebaseUtils.email)
                                .collection("resolved").document(String.valueOf(task.getResult().getValue()))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists() && documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1)) != null) {
                                    currNivel.setCurent(String.valueOf(documentSnapshot.get(String.valueOf(Integer.parseInt(currNivel.getId()) - 1))));
                                } else
                                    currNivel.setCurent("0");


                                adapter = new NivelSelectSlideAdapter(nivels, categorie, categorieId, NivelSelect.this);
                                viewPager.setAdapter(adapter);


                            }
                        });
                    } else if (currNivel.getId().equals("0")) currNivel.setCurent("0");
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NivelSelect.this, SelectGeneralKnowlage.class);
        startActivity(intent);
    }
}