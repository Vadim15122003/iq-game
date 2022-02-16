package project.rew.iqgamequiz.utils;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import project.rew.iqgamequiz.Question;
import project.rew.iqgamequiz.Questions;
import project.rew.iqgamequiz.R;

import static project.rew.iqgamequiz.LoginActivity.mAuth;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questions = new ArrayList<>();
    ViewPager2 viewPager;

    public QuestionAdapter(List<Question> questions, ViewPager2 viewPager) {
        this.questions = questions;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_question, parent, false);

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
            if (cureentQuestion.getAnsewrs().get(0).isCorect()) {
                holder.v1.setVisibility(View.VISIBLE);
            } else holder.r1.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.v1.setVisibility(View.GONE);
                    holder.r1.setVisibility(View.GONE);
                    viewPager.setCurrentItem(position + 1);
                }
            }, 700);
        });
        holder.i2.setOnClickListener(v -> {
            if (cureentQuestion.getAnsewrs().get(1).isCorect()) {
                holder.v2.setVisibility(View.VISIBLE);
            } else holder.r2.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.v2.setVisibility(View.GONE);
                    holder.r2.setVisibility(View.GONE);
                    viewPager.setCurrentItem(position + 1);
                }
            }, 700);
        });
        holder.i3.setOnClickListener(v -> {
            if (cureentQuestion.getAnsewrs().get(2).isCorect()) {
                holder.v3.setVisibility(View.VISIBLE);
            } else holder.r3.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.v3.setVisibility(View.GONE);
                    holder.r3.setVisibility(View.GONE);
                    viewPager.setCurrentItem(position + 1);
                }
            }, 700);
        });
        holder.i4.setOnClickListener(v -> {
            if (cureentQuestion.getAnsewrs().get(3).isCorect()) {
                holder.v4.setVisibility(View.VISIBLE);
            } else holder.r4.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.v4.setVisibility(View.GONE);
                    holder.r4.setVisibility(View.GONE);
                    viewPager.setCurrentItem(position + 1);
                }
            }, 700);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(questions.size(), 10);
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
