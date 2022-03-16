package project.rew.iqgamequiz.mainactivities.play.questions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.GeneralAtributes;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.adapters.QuestionAdapter;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Answer;
import project.rew.iqgamequiz.mainactivities.play.questions.items.NivelAtributes;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Question;
import project.rew.iqgamequiz.utils.FirebaseUtils;


public class QuestionsActivity extends AppCompatActivity {
    DatabaseReference ref;
    String categorie, categorieId;
    ViewPager2 viewPager;
    QuestionAdapter adapter;
    TextView textView, coins, glory, tdouble_change, tswichq, tcinzeci, tcorect, txt_exit, txt_restart;
    List<Question> questions = new ArrayList<>();
    CardView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, double_change, swichq, cinzeci, corect;
    List<CardView> cards = new ArrayList<>();
    ImageView pause, resume, restart, exit, e_yes, e_no, r_yes, r_no, img_double_change, img_swichq,
            img_cinzeci, img_corect, dialog_exit_img, dialog_restart_img, dialog_pause_img;
    Dialog d_pause, d_exit, d_restart;
    Window w_pause, w_exit, w_restart;
    public static List<GivenReward> givenRewards;
    GeneralAtributes generalAtributes;
    NivelAtributes nivelAtributes;
    ConstraintLayout questionLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewPager = findViewById(R.id.viewpager);
        Bundle bundle = getIntent().getBundleExtra("Bundles");
        categorie = bundle.getString("categorie");
        generalAtributes = bundle.getParcelable("general_atributes");
        nivelAtributes = bundle.getParcelable("nivel_atributes");
        categorieId = bundle.getString("categorieId");
        int nivel = bundle.getInt("nivel", 0);
        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        questionLay = findViewById(R.id.questonLay);
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
        double_change = findViewById(R.id.double_chance);
        swichq = findViewById(R.id.swichq);
        cinzeci = findViewById(R.id.cinzeci);
        corect = findViewById(R.id.corect);
        tdouble_change = findViewById(R.id.tdoublechange);
        tswichq = findViewById(R.id.tswichq);
        tcinzeci = findViewById(R.id.tcinzeci);
        tcorect = findViewById(R.id.tcorect);
        img_double_change = findViewById(R.id.img_double_change);
        img_swichq = findViewById(R.id.img_swichq);
        img_cinzeci = findViewById(R.id.img_cinzeci);
        img_corect = findViewById(R.id.img_corect);
        pause = findViewById(R.id.pause);
        d_pause = new Dialog(QuestionsActivity.this, R.style.DialogTransparentBg);
        d_exit = new Dialog(QuestionsActivity.this, R.style.DialogTransparentBg);
        d_restart = new Dialog(QuestionsActivity.this, R.style.DialogTransparentBg);
        d_pause.setContentView(R.layout.dialog_pause);
        d_exit.setContentView(R.layout.dialog_quit);
        d_restart.setContentView(R.layout.dialog_restart);
        d_pause.setCancelable(false);
        d_restart.setCancelable(false);
        d_exit.setCancelable(false);
        w_pause = d_pause.getWindow();
        w_exit = d_exit.getWindow();
        w_restart = d_restart.getWindow();
        WindowManager.LayoutParams lp_pause = w_pause.getAttributes();
        WindowManager.LayoutParams lp_exit = w_exit.getAttributes();
        WindowManager.LayoutParams lp_restart = w_restart.getAttributes();
        lp_pause.dimAmount = 0.8f;
        lp_exit.dimAmount = 0.8f;
        lp_restart.dimAmount = 0.8f;
        w_pause.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w_exit.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w_restart.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w_pause.setGravity(Gravity.CENTER);
        w_restart.setGravity(Gravity.CENTER);
        w_exit.setGravity(Gravity.CENTER);
        w_pause.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w_exit.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w_restart.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w_pause.getAttributes().windowAnimations = R.style.DiaolgPAUSE;
        w_exit.getAttributes().windowAnimations = R.style.DialogAnimation;
        w_restart.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_exit_img = d_exit.findViewById(R.id.imageExit);
        dialog_restart_img = d_restart.findViewById(R.id.imageRestart);
        dialog_pause_img = d_pause.findViewById(R.id.pause_bckg_img);
        resume = d_pause.findViewById(R.id.resume);
        restart = d_pause.findViewById(R.id.restart);
        exit = d_pause.findViewById(R.id.exit);
        e_yes = d_exit.findViewById(R.id.yes);
        e_no = d_exit.findViewById(R.id.no);
        r_yes = d_restart.findViewById(R.id.yes);
        r_no = d_restart.findViewById(R.id.no);
        txt_exit = d_exit.findViewById(R.id.txt_exit);
        txt_restart = d_restart.findViewById(R.id.txt_restart);

        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        cards.add(c5);
        cards.add(c6);
        cards.add(c7);
        cards.add(c8);
        cards.add(c9);
        cards.add(c10);

        glory.setText(FirebaseUtils.glory);
        coins.setText(FirebaseUtils.coins);

        if (nivelAtributes != null) {
            loadBackground();
            if (nivelAtributes.getDouble_change_selected() != null)
                Picasso.get().load(nivelAtributes.getDouble_change_selected()).into(img_double_change);
            if (nivelAtributes.getSwich_selected_img() != null)
                Picasso.get().load(nivelAtributes.getSwich_selected_img()).into(img_swichq);
            if (nivelAtributes.getCorect_selected() != null)
                Picasso.get().load(nivelAtributes.getCorect_selected()).into(img_corect);
            if (nivelAtributes.getCinzeci_selected() != null)
                Picasso.get().load(nivelAtributes.getCinzeci_selected()).into(img_cinzeci);
            if (nivelAtributes.getDialog_exit_img() != null) {
                Picasso.get().load(nivelAtributes.getDialog_exit_img()).into(dialog_exit_img);
                dialog_exit_img.setAdjustViewBounds(true);
            }
            if (nivelAtributes.getDialog_exit_no() != null)
                Picasso.get().load(nivelAtributes.getDialog_exit_no()).into(e_no);
            if (nivelAtributes.getDialog_exit_yes() != null)
                Picasso.get().load(nivelAtributes.getDialog_exit_yes()).into(e_yes);
            if (nivelAtributes.getDialog_exit_txt_color() != null)
                txt_exit.setTextColor(Color.parseColor(nivelAtributes.getDialog_exit_txt_color()));
            if (nivelAtributes.getDialog_restart_img() != null) {
                Picasso.get().load(nivelAtributes.getDialog_restart_img()).into(dialog_restart_img);
                dialog_restart_img.setAdjustViewBounds(true);
            }
            if (nivelAtributes.getDialog_restart_no() != null)
                Picasso.get().load(nivelAtributes.getDialog_restart_no()).into(r_no);
            if (nivelAtributes.getDialog_restart_yes() != null)
                Picasso.get().load(nivelAtributes.getDialog_restart_yes()).into(r_yes);
            if (nivelAtributes.getDialog_restart_txt_color() != null)
                txt_restart.setTextColor(Color.parseColor(nivelAtributes.getDialog_restart_txt_color()));
            if (nivelAtributes.getExit_btn() != null)
                Picasso.get().load(nivelAtributes.getExit_btn()).into(exit);
            if (nivelAtributes.getPause_btn() != null)
                Picasso.get().load(nivelAtributes.getPause_btn()).into(pause);
            if (nivelAtributes.getPause_dialog_img() != null) {
                Picasso.get().load(nivelAtributes.getPause_dialog_img()).into(dialog_pause_img);
                dialog_pause_img.setAdjustViewBounds(true);
            }
        }

        ref = FirebaseDatabase.getInstance().getReference().child("RO").child("Categories").child(categorie).child("nivels").child(String.valueOf(nivel)).child("questions");

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
                adapter = new QuestionAdapter(questions, viewPager, cards, QuestionsActivity.this,
                        categorie, categorieId, String.valueOf(nivel), coins, glory, double_change, swichq, cinzeci, corect,
                        img_double_change, img_swichq, img_cinzeci, img_corect,
                        tdouble_change, tswichq, tcinzeci, tcorect, generalAtributes, nivelAtributes);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        setDialogs();
        pause.setOnClickListener(v -> {
            d_pause.show();
        });
    }

    public void setDialogs() {
        resume.setOnClickListener(v -> {
            d_pause.cancel();
        });
        restart.setOnClickListener(v -> {
            d_restart.show();
        });
        exit.setOnClickListener(v -> {
            d_exit.show();
        });
        e_yes.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionsActivity.this, NivelSelectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categorie", categorie);
            bundle.putParcelable("general_atributes", generalAtributes);
            intent.putExtra("Bundles", bundle);
            startActivity(intent);
            finish();
        });
        e_no.setOnClickListener(v -> {
            d_exit.cancel();
        });
        r_yes.setOnClickListener(v -> {
            recreate();
            viewPager.setCurrentItem(0);
        });
        r_no.setOnClickListener(v -> {
            d_restart.cancel();
        });
    }

    @Override
    public void onBackPressed() {

    }

    void loadBackground() {
        if (nivelAtributes.getQuestion_bckg() != null)
            Picasso.get().load(nivelAtributes.getQuestion_bckg()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    questionLay.setBackground(new BitmapDrawable(bitmap));
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