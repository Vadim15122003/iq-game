package project.rew.iqgamequiz.mainactivities.friends.personaltest.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.friendprofile.FriendProfileActivity;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.mainactivities.friends.personaltest.items.PersonalQuestion;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class PersonalQuestionAdapter extends RecyclerView.Adapter<PersonalQuestionAdapter.ViewHolder> {

    List<PersonalQuestion> questions = new ArrayList<>();
    Friend friend;
    ViewPager2 viewPager;
    List<CardView> cards;
    Dialog finish;
    Context context;
    ImageView back;
    TextView coins, dialog_coins;
    boolean clicked = false;
    int corecte, gresite;

    public PersonalQuestionAdapter(List<PersonalQuestion> questions, ViewPager2 viewPager,
                                   List<CardView> cards, Context context,
                                   TextView coins,Friend friend) {
        this.questions = questions;
        this.viewPager = viewPager;
        this.cards = cards;
        this.context = context;
        this.coins = coins;
        this.friend = friend;
        clicked = false;
        corecte = 0;
        gresite = 0;
    }

    @NonNull
    @Override
    public PersonalQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_personal_question, parent, false);
        finish = new Dialog(context, R.style.DialogTransparentBg);
        finish.setContentView(R.layout.dialog_finish_question_personal);
        finish.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finish.setCancelable(false);
        WindowManager.LayoutParams lp = finish.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        finish.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        finish.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        back = finish.findViewById(R.id.back);
        dialog_coins = finish.findViewById(R.id.coins);

        back.setOnClickListener(v -> {
            goToBackActivity();
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalQuestionAdapter.ViewHolder holder, int position) {
        PersonalQuestion cureentQuestion = questions.get(position);
        holder.question.setText(cureentQuestion.getQuestion());
        holder.t1.setText(cureentQuestion.getAnsewrs().get(0).getAnswer());
        holder.t2.setText(cureentQuestion.getAnsewrs().get(1).getAnswer());
        holder.t3.setText(cureentQuestion.getAnsewrs().get(2).getAnswer());
        holder.t4.setText(cureentQuestion.getAnsewrs().get(3).getAnswer());
        ImageView vCorect = getCorect(holder.v1, holder.v2, holder.v3, holder.v4, cureentQuestion.getCorectAnswerPosition());
        holder.i1.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(0).isCorect(), holder.v1, vCorect, holder.r1, viewPager, position);
        });
        holder.i2.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(1).isCorect(), holder.v2, vCorect, holder.r2, viewPager, position);
        });
        holder.i3.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(2).isCorect(), holder.v3, vCorect, holder.r3, viewPager, position);
        });
        holder.i4.setOnClickListener(v -> {
            AnswerSelect(cureentQuestion.getAnsewrs().get(3).isCorect(), holder.v4, vCorect, holder.r4, viewPager, position);
        });

    }

    private ImageView getCorect(ImageView v1, ImageView v2, ImageView v3, ImageView v4, int corectAnswPos) {
        switch (corectAnswPos) {
            case 0:
                return v1;
            case 1:
                return v2;
            case 2:
                return v3;
            case 3:
                return v4;
            default:
                return v1;
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(questions.size(), 20);
    }

    public void AnswerSelect(boolean corect, ImageView v, ImageView vCorect, ImageView r, ViewPager2 viewPager, int position) {
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
            if (v != vCorect) vCorect.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.GONE);
                    r.setVisibility(View.GONE);
                    vCorect.setVisibility(View.GONE);
                    viewPager.setCurrentItem(position + 1);
                    clicked = false;
                    if (position == 19) {
                        finish.show();
                        FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) +
                                corecte * 2, coins);
                        dialog_coins.setText(String.valueOf(corecte * 2));
                    }
                }
            }, 1200);
        }
    }

    public void goToBackActivity() {
        Intent intent = new Intent(context, FriendProfileActivity.class);
        intent.putExtra("friend",friend);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView i1, i2, i3, i4;
        TextView question, t1, t2, t3, t4;
        ImageView r1, r2, r3, r4, v1, v2, v3, v4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
