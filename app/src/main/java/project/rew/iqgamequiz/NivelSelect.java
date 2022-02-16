package project.rew.iqgamequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.utils.NivelSelectSlideAdapter;

public class NivelSelect extends AppCompatActivity {

    DatabaseReference ref;
    ViewPager2 viewPager;
    NivelSelectSlideAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel_select);

        viewPager = findViewById(R.id.viewpager);
        String categorie=getIntent().getStringExtra("categorie");
        ref = FirebaseDatabase.getInstance().getReference().child("Categories").child(categorie).child("nivels");
        List<String> nivels = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nivels.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dss:snapshot.getChildren()) {
                        nivels.add(dss.getKey().toString());
                    }
                    adapter=new NivelSelectSlideAdapter(nivels,categorie,NivelSelect.this);
                    viewPager.setAdapter(adapter);
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
}