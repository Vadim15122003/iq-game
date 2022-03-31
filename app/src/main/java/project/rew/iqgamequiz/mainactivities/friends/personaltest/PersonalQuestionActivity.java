package project.rew.iqgamequiz.mainactivities.friends.personaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.friendprofile.FriendProfileActivity;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.mainactivities.friends.personaltest.adapters.PersonalQuestionAdapter;
import project.rew.iqgamequiz.mainactivities.friends.personaltest.items.PersonalQuestion;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.adapters.QuestionAdapter;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Answer;
import project.rew.iqgamequiz.utils.FirebaseUtils;


public class PersonalQuestionActivity extends AppCompatActivity {

    String email;
    FirebaseFirestore fstroe;
    List<PersonalQuestion> questions = new ArrayList<>();
    CardView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20;
    TextView coins, glory;
    ViewPager2 viewPager;
    List<CardView> cards = new ArrayList<>();
    PersonalQuestionAdapter adapter;
    Dialog d_pause, d_exit;
    ImageView pause, resume, exit, e_yes, e_no,dialog_exit_img, dialog_pause_img;
    Window w_pause, w_exit;
    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_question);
        email = getIntent().getStringExtra("email");
        fstroe = FirebaseFirestore.getInstance();
        friend = getIntent().getParcelableExtra("friend");

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        c9 = findViewById(R.id.c9);
        c10 = findViewById(R.id.c10);
        c11 = findViewById(R.id.c11);
        c12 = findViewById(R.id.c12);
        c13 = findViewById(R.id.c13);
        c14 = findViewById(R.id.c14);
        c15 = findViewById(R.id.c15);
        c16 = findViewById(R.id.c16);
        c17 = findViewById(R.id.c17);
        c18 = findViewById(R.id.c18);
        c19 = findViewById(R.id.c19);
        c20 = findViewById(R.id.c20);
        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        viewPager = findViewById(R.id.viewpager);

        pause = findViewById(R.id.pause);
        d_pause = new Dialog(PersonalQuestionActivity.this, R.style.DialogTransparentBg);
        d_exit = new Dialog(PersonalQuestionActivity.this, R.style.DialogTransparentBg);
        d_pause.setContentView(R.layout.dialog_pause_personal);
        d_exit.setContentView(R.layout.dialog_quit_personal);
        d_pause.setCancelable(false);
        d_exit.setCancelable(false);
        w_pause = d_pause.getWindow();
        w_exit = d_exit.getWindow();
        WindowManager.LayoutParams lp_pause = w_pause.getAttributes();
        WindowManager.LayoutParams lp_exit = w_exit.getAttributes();
        lp_pause.dimAmount = 0.8f;
        lp_exit.dimAmount = 0.8f;
        w_pause.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w_exit.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w_pause.setGravity(Gravity.CENTER);
        w_exit.setGravity(Gravity.CENTER);
        w_pause.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w_exit.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w_pause.getAttributes().windowAnimations = R.style.DiaolgPAUSE;
        w_exit.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_exit_img = d_exit.findViewById(R.id.imageExit);
        dialog_pause_img = d_pause.findViewById(R.id.pause_bckg_img);
        resume = d_pause.findViewById(R.id.resume);
        exit = d_pause.findViewById(R.id.exit);
        e_yes = d_exit.findViewById(R.id.yes);
        e_no = d_exit.findViewById(R.id.no);

        cards.addAll(new ArrayList<>(Arrays.asList(c1,c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20)));
        glory.setText(FirebaseUtils.glory);
        coins.setText(FirebaseUtils.coins);

        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        setDialogs();
        pause.setOnClickListener(v -> {
            d_pause.show();
        });
        getQuestions();
    }

    public void setDialogs() {
        resume.setOnClickListener(v -> {
            d_pause.cancel();
        });
        exit.setOnClickListener(v -> {
            d_exit.show();
        });
        e_yes.setOnClickListener(v -> {
            d_exit.cancel();
            d_pause.cancel();
            Intent intent = new Intent(PersonalQuestionActivity.this, FriendProfileActivity.class);
            intent.putExtra("friend",friend);
            startActivity(intent);
        });
        e_no.setOnClickListener(v -> {
            d_exit.cancel();
        });
    }

    private void getQuestions() {
        fstroe.collection("users").document("vadim-plamadeala@mail.ru")
                .collection("test_personal").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        questions.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            PersonalQuestion personalQuestion = new PersonalQuestion();
                            personalQuestion.setQuestion(snapshot.getId().toString());
                            List<Answer> answers = new ArrayList<>();
                            Map<String, Object> data = snapshot.getData();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                answers.add(new Answer(entry.getKey().toString(), Boolean.valueOf(entry.getValue().toString())));
                            }
                            Collections.shuffle(answers);
                            personalQuestion.setAnsewrs(answers);
                            questions.add(personalQuestion);
                        }
                        Collections.shuffle(questions);
                        adapter = new PersonalQuestionAdapter(questions, viewPager, cards,
                                PersonalQuestionActivity.this,coins,friend);
                        viewPager.setAdapter(adapter);
                    }
                });
    }
}