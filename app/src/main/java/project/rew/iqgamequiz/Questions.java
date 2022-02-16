package project.rew.iqgamequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.utils.NivelSelectSlideAdapter;
import project.rew.iqgamequiz.utils.QuestionAdapter;


public class Questions extends AppCompatActivity {
    DatabaseReference ref;
    ViewPager2 viewPager;
    QuestionAdapter adapter;
    TextView textView;
    List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        viewPager = findViewById(R.id.viewpager);
        String categorie = getIntent().getStringExtra("categorie");
        int nivel = getIntent().getIntExtra("nivel", 0);
        textView = findViewById(R.id.text);

        ref = FirebaseDatabase.getInstance().getReference().child("Categories").child(categorie).child("nivels").child(String.valueOf(nivel)).child("questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questions.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        Map<String, Object> temp = new HashMap<>();
                        List<Answer> answers = new ArrayList<>();
                        String question = dss.getKey();
                        temp.put(dss.getKey(), dss.getValue());
                        temp = (Map<String, Object>) temp.get(dss.getKey());
                        String id = String.valueOf(temp.get("id"));
                        String image = String.valueOf(temp.get("image"));

                        for (String key : temp.keySet()) {
                            if (!key.equals("id") && !key.equals("image")) {
                                answers.add(new Answer(String.valueOf(key), (boolean) temp.get(key)));
                            }
                        }
                        Collections.shuffle(answers);
                        questions.add(new Question(question, image, id, answers));
                    }
                    Collections.shuffle(questions);
                }
                adapter=new QuestionAdapter(questions,viewPager);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }
}