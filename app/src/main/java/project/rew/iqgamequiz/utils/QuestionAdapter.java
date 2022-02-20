package project.rew.iqgamequiz.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.NivelSelect;
import project.rew.iqgamequiz.Question;
import project.rew.iqgamequiz.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questions = new ArrayList<>();
    ViewPager2 viewPager;
    List<CardView> cards;
    Dialog finish;
    Context context;
    ImageView back, replay;
    String categorie;
    TextView coins, glory, dialog_coins, dialog_glory;
    boolean clicked = false;
    int corecte, gresite;

    public QuestionAdapter(List<Question> questions, ViewPager2 viewPager,
                           List<CardView> cards, Context context, String categorie,
                           TextView coins, TextView glory) {
        this.questions = questions;
        this.viewPager = viewPager;
        this.cards = cards;
        this.context = context;
        this.categorie = categorie;
        this.coins = coins;
        this.glory = glory;
        corecte = 0;
        gresite = 0;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_question, parent, false);
        finish = new Dialog(context, R.style.Dialog);
        finish.setContentView(R.layout.dialog_finish_question);
        finish.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finish.setCancelable(false);
        WindowManager.LayoutParams lp = finish.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        finish.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        finish.getWindow().getAttributes().windowAnimations = R.style.animation;

        back = finish.findViewById(R.id.back);
        replay = finish.findViewById(R.id.replay);
        dialog_coins=finish.findViewById(R.id.coins);
        dialog_glory=finish.findViewById(R.id.glory);
        back.setOnClickListener(v -> {
            goToBackActivity();
        });
        replay.setOnClickListener(v -> {
            ((Activity) context).recreate();
            viewPager.setCurrentItem(0);
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        Question cureentQuestion = questions.get(position);
        if (cureentQuestion.getImage() != null)
            Picasso.get().load(cureentQuestion.getImage()).into(holder.image);
        holder.question.setText(cureentQuestion.getQuestion());
        holder.t1.setText(cureentQuestion.getAnsewrs().get(0).getAnswer());
        holder.t2.setText(cureentQuestion.getAnsewrs().get(1).getAnswer());
        holder.t3.setText(cureentQuestion.getAnsewrs().get(2).getAnswer());
        holder.t4.setText(cureentQuestion.getAnsewrs().get(3).getAnswer());

        holder.i1.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(0).isCorect(), holder.v1, holder.r1, viewPager, position);
        });
        holder.i2.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(1).isCorect(), holder.v2, holder.r2, viewPager, position);
        });
        holder.i3.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(2).isCorect(), holder.v3, holder.r3, viewPager, position);
        });
        holder.i4.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(3).isCorect(), holder.v4, holder.r4, viewPager, position);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(questions.size(), 10);
    }

    public void AnswerSelect(boolean corect, LinearLayout v, LinearLayout r, ViewPager2 viewPager, int position) {
        if (!clicked) {
            clicked = true;
            if (corect) {
                corecte++;
                v.setVisibility(View.VISIBLE);
                cards.get(position).setCardBackgroundColor(Color.parseColor("#0EAF08"));
            } else {
                gresite++;
                r.setVisibility(View.VISIBLE);
                cards.get(position).setCardBackgroundColor(Color.parseColor("#ED2828"));
            }

            if (gresite == 3) {
                finish.show();
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.GONE);
                        r.setVisibility(View.GONE);
                        viewPager.setCurrentItem(position + 1);
                        clicked = false;
                        if (position == 9) {
                            finish.show();
                            Constants.addCoins(Constants.getInt(Constants.coins) + corecte * 2, coins);
                            Constants.addGlory(Constants.getInt(Constants.glory) + corecte, glory);
                            dialog_coins.setText(String.valueOf(corecte * 2));
                            dialog_glory.setText(String.valueOf(corecte));
                        }
                    }
                }, 700);
            }
        }
    }

    public void goToBackActivity() {
        Intent intent = new Intent(context, NivelSelect.class);
        intent.putExtra("categorie", categorie);
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, i1, i2, i3, i4;
        TextView question, t1, t2, t3, t4;
        LinearLayout r1, r2, r3, r4, v1, v2, v3, v4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            question = itemView.findViewById(R.id.tq);
            i1 = itemView.findViewById(R.id.i1);
            i2 = itemView.findViewById(R.id.i2);
            i3 = itemView.findViewById(R.id.i3);
            i4 = itemView.findViewById(R.id.i4);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
            t4 = itemView.findViewById(R.id.t4);
            r1 = itemView.findViewById(R.id.r1);
            r2 = itemView.findViewById(R.id.r2);
            r3 = itemView.findViewById(R.id.r3);
            r4 = itemView.findViewById(R.id.r4);
            v1 = itemView.findViewById(R.id.v1);
            v2 = itemView.findViewById(R.id.v2);
            v3 = itemView.findViewById(R.id.v3);
            v4 = itemView.findViewById(R.id.v4);
        }
    }
}
