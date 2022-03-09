package project.rew.iqgamequiz.mainactivities.play.questions.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.transition.TransitionManager;
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


import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.RewardType;
import project.rew.iqgamequiz.mainactivities.play.questions.items.Question;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questions = new ArrayList<>();
    ViewPager2 viewPager;
    List<CardView> cards;
    Dialog finish;
    Context context;
    ImageView back, replay, img_double_change, img_swichq, img_cinzeci, img_corect, profileImg, logoTitle;
    String categorie, categorieId, nivelId;
    TextView coins, glory, dialog_coins, dialog_glory, tdouble_change, tswichq, tcinzeci, tcorect, titleRewardTV;
    boolean clicked = false, bdouble_change, bcorect = false, bcinzeci = false;
    int corecte, gresite;
    CardView double_change, swichq, cinzeci, corect, profileImgReward, titleReward;
    List<Integer> answersnr = new ArrayList<>();

    public QuestionAdapter(List<Question> questions, ViewPager2 viewPager,
                           List<CardView> cards, Context context, String categorie,
                           String categorieId, String nivelId,
                           TextView coins, TextView glory, CardView double_change,
                           CardView swichq, CardView cinzeci, CardView corect,
                           ImageView img_double_change, ImageView img_swichq,
                           ImageView img_cinzeci, ImageView img_corect, TextView tdouble_change,
                           TextView tswichq, TextView tcinzeci, TextView tcorect) {
        this.questions = questions;
        this.viewPager = viewPager;
        this.cards = cards;
        this.context = context;
        this.categorie = categorie;
        this.categorieId = categorieId;
        this.nivelId = nivelId;
        this.coins = coins;
        this.glory = glory;
        this.double_change = double_change;
        this.swichq = swichq;
        this.cinzeci = cinzeci;
        this.corect = corect;
        this.img_double_change = img_double_change;
        this.img_swichq = img_swichq;
        this.img_cinzeci = img_cinzeci;
        this.img_corect = img_corect;
        this.tdouble_change = tdouble_change;
        this.tswichq = tswichq;
        this.tcinzeci = tcinzeci;
        this.tcorect = tcorect;
        corecte = 0;
        gresite = 0;
        answersnr.add(1);
        answersnr.add(2);
        answersnr.add(3);
        answersnr.add(4);
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_question, parent, false);
        finish = new Dialog(context, R.style.DialogTransparentBg);
        finish.setContentView(R.layout.dialog_finish_question);
        finish.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finish.setCancelable(false);
        WindowManager.LayoutParams lp = finish.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        finish.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        finish.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        back = finish.findViewById(R.id.back);
        replay = finish.findViewById(R.id.replay);
        dialog_coins = finish.findViewById(R.id.coins);
        dialog_glory = finish.findViewById(R.id.glory);
        profileImgReward = finish.findViewById(R.id.cardView);
        titleReward = finish.findViewById(R.id.title);
        titleRewardTV = finish.findViewById(R.id.title_select);
        profileImg = finish.findViewById(R.id.profile_img_select);
        logoTitle = finish.findViewById(R.id.title_logo_select);
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
        setVisibilityToAnswers(holder.t1, holder.t2, holder.t3, holder.t4, holder.i1, holder.i2, holder.i3, holder.i4);
        takeVisibility(holder.v1, holder.v2, holder.v3, holder.v4, holder.r1, holder.r2, holder.r3, holder.r4);
        if (cureentQuestion.getImage() != null)
            Picasso.get().load(cureentQuestion.getImage()).into(holder.image);
        holder.question.setText(cureentQuestion.getQuestion());
        holder.t1.setText(cureentQuestion.getAnsewrs().get(0).getAnswer());
        holder.t2.setText(cureentQuestion.getAnsewrs().get(1).getAnswer());
        holder.t3.setText(cureentQuestion.getAnsewrs().get(2).getAnswer());
        holder.t4.setText(cureentQuestion.getAnsewrs().get(3).getAnswer());
        holder.double_change.setOnClickListener(v -> {
            if (img_double_change.getDrawable().getConstantState() != context.getResources().getDrawable(R.drawable.clicked_double_change).getConstantState()) {
                img_double_change.setImageResource(R.drawable.clicked_double_change);
                FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) - FirebaseUtils.getInt(String.valueOf(tdouble_change.getText())), coins);
                bdouble_change = true;
            }
        });
        holder.swichq.setOnClickListener(v -> {
            if (img_swichq.getDrawable().getConstantState() != context.getResources().getDrawable(R.drawable.clicked_swichq).getConstantState()) {
                img_swichq.setImageResource(R.drawable.clicked_swichq);
                clicked = true;
                FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) - FirebaseUtils.getInt(String.valueOf(tswichq.getText())), coins);
                questions.add(questions.get(viewPager.getCurrentItem()));
                questions.remove(viewPager.getCurrentItem());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bdouble_change = false;
                        takeVisibility(holder.v1, holder.v2, holder.v3, holder.v4, holder.r1, holder.r2, holder.r3, holder.r4);
                        TransitionManager.beginDelayedTransition(holder.viewGroup);
                        holder.question.setVisibility(View.INVISIBLE);
                        holder.t1.setVisibility(View.INVISIBLE);
                        holder.t2.setVisibility(View.INVISIBLE);
                        holder.t3.setVisibility(View.INVISIBLE);
                        holder.t4.setVisibility(View.INVISIBLE);
                        holder.image.setVisibility(View.INVISIBLE);
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TransitionManager.beginDelayedTransition(holder.viewGroup);
                                Question cureentQuestion1 = questions.get(viewPager.getCurrentItem());
                                if (cureentQuestion1.getImage() != null)
                                    Picasso.get().load(cureentQuestion1.getImage()).into(holder.image);
                                setVisibilityToAnswers(holder.t1, holder.t2, holder.t3, holder.t4, holder.i1, holder.i2, holder.i3, holder.i4);
                                holder.question.setText(cureentQuestion1.getQuestion());
                                holder.t1.setText(cureentQuestion1.getAnsewrs().get(0).getAnswer());
                                holder.t2.setText(cureentQuestion1.getAnsewrs().get(1).getAnswer());
                                holder.t3.setText(cureentQuestion1.getAnsewrs().get(2).getAnswer());
                                holder.t4.setText(cureentQuestion1.getAnsewrs().get(3).getAnswer());
                                holder.question.setVisibility(View.VISIBLE);
                                holder.t1.setVisibility(View.VISIBLE);
                                holder.t2.setVisibility(View.VISIBLE);
                                holder.t3.setVisibility(View.VISIBLE);
                                holder.t4.setVisibility(View.VISIBLE);
                                holder.image.setVisibility(View.VISIBLE);
                                clicked = false;
                            }
                        }, 500);
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        }, 700);
                    }
                }, 400);
            }
        });
        holder.corect.setOnClickListener(v -> {
            if (img_corect.getDrawable().getConstantState() != context.getResources().getDrawable(R.drawable.clicked_corect).getConstantState() && !bcinzeci) {
                img_corect.setImageResource(R.drawable.clicked_corect);
                bcorect = true;
                FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) - FirebaseUtils.getInt(String.valueOf(tcorect.getText())), coins);
                TransitionManager.beginDelayedTransition(holder.viewGroup);
                if (!cureentQuestion.getAnsewrs().get(0).isCorect()) {
                    holder.t1.setVisibility(View.INVISIBLE);
                    holder.i1.setVisibility(View.INVISIBLE);
                }
                if (!cureentQuestion.getAnsewrs().get(1).isCorect()) {
                    holder.t2.setVisibility(View.INVISIBLE);
                    holder.i2.setVisibility(View.INVISIBLE);
                }
                if (!cureentQuestion.getAnsewrs().get(2).isCorect()) {
                    holder.t3.setVisibility(View.INVISIBLE);
                    holder.i3.setVisibility(View.INVISIBLE);
                }
                if (!cureentQuestion.getAnsewrs().get(3).isCorect()) {
                    holder.t4.setVisibility(View.INVISIBLE);
                    holder.i4.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.cinzeci.setOnClickListener(v -> {
            if (img_cinzeci.getDrawable().getConstantState() != context.getResources().getDrawable(R.drawable.clicked_cinzeci).getConstantState() && !bcorect) {
                img_cinzeci.setImageResource(R.drawable.clicked_cinzeci);
                FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) - FirebaseUtils.getInt(String.valueOf(tcinzeci.getText())), coins);
                bcinzeci = true;
                Collections.shuffle(answersnr);
                int sterse = 0;
                TransitionManager.beginDelayedTransition(holder.viewGroup);
                for (int j : answersnr) {
                    if (sterse == 2) break;
                    if (!cureentQuestion.getAnsewrs().get(j - 1).isCorect()) {
                        sterse++;
                        if (j == 1) {
                            holder.t1.setVisibility(View.INVISIBLE);
                            holder.i1.setVisibility(View.INVISIBLE);
                        } else if (j == 2) {
                            holder.t2.setVisibility(View.INVISIBLE);
                            holder.i2.setVisibility(View.INVISIBLE);
                        } else if (j == 3) {
                            holder.t3.setVisibility(View.INVISIBLE);
                            holder.i3.setVisibility(View.INVISIBLE);
                        } else if (j == 4) {
                            holder.t4.setVisibility(View.INVISIBLE);
                            holder.i4.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
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

    public void AnswerSelect(boolean corect, ImageView v, ImageView r, ViewPager2 viewPager, int position) {
        if (!clicked) {
            boolean gotoNext = true;
            if (!bdouble_change)
                clicked = true;
            if (corect) {
                clicked = true;
                corecte++;
                v.setVisibility(View.VISIBLE);
                cards.get(position).setCardBackgroundColor(Color.parseColor("#0EAF08"));
            } else {
                if (!bdouble_change) {
                    gresite++;
                    r.setVisibility(View.VISIBLE);
                    cards.get(position).setCardBackgroundColor(Color.parseColor("#ED2828"));
                } else {
                    gotoNext = false;
                    r.setVisibility(View.VISIBLE);
                }
            }

            if (gresite == 3) {
                finish.show();
                dialog_coins.setText("0");
                dialog_glory.setText("0");
            } else if (gotoNext) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.GONE);
                        r.setVisibility(View.GONE);
                        img_corect.setImageResource(R.drawable.corect);
                        img_double_change.setImageResource(R.drawable.doublechange);
                        img_cinzeci.setImageResource(R.drawable.cinzeci);
                        img_swichq.setImageResource(R.drawable.reload);
                        bdouble_change = false;
                        viewPager.setCurrentItem(position + 1);
                        if (bcorect || bcinzeci) {
                            notifyDataSetChanged();
                            bcorect = false;
                            bcinzeci = false;
                        }
                        clicked = false;
                        if (position == 9) {
                            finish.show();
                            FirebaseUtils.addCoins(FirebaseUtils.getInt(FirebaseUtils.coins) + corecte * 2, coins);
                            FirebaseUtils.addGlory(FirebaseUtils.getInt(FirebaseUtils.glory) + corecte, glory);
                            FirebaseUtils.setCorectAnswers(categorieId, nivelId, corecte);
                            dialog_coins.setText(String.valueOf(corecte * 2));
                            dialog_glory.setText(String.valueOf(corecte));
                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                            fstore.collection("users").document(FirebaseUtils.email)
                                    .collection("resolved").document(categorieId)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    for (GivenReward givenReward : QuestionsActivity.givenRewards) {
                                        if (!givenReward.isClaimed()) {
                                            int curentPoints = 0;
                                            if (documentSnapshot.exists() && documentSnapshot.get(nivelId) != null)
                                                curentPoints = FirebaseUtils.getInt(String.valueOf(documentSnapshot.get(nivelId)));
                                            if (curentPoints + corecte
                                                    >= FirebaseUtils.getInt(givenReward.getPointsNedeed())) {
                                                givenReward.setClaimed(true);
                                                FirebaseUtils.setRewardClaimed(categorieId, nivelId, givenReward.getId());
                                                if (givenReward.getRewardType() == RewardType.ProfileImage) {
                                                    FirebaseUtils.addProfileImage(givenReward.getProfileImage().getId());
                                                    profileImgReward.setVisibility(View.VISIBLE);
                                                    Picasso.get().load(givenReward.getProfileImage().getImage())
                                                            .into(profileImg);
                                                }
                                                if (givenReward.getRewardType() == RewardType.Title) {
                                                    FirebaseUtils.addTitle(givenReward.getTitle().getId());
                                                    titleReward.setVisibility(View.VISIBLE);
                                                    Picasso.get().load(givenReward.getTitle().getLogo())
                                                            .into(logoTitle);
                                                    titleRewardTV.setText(givenReward.getTitle().getTitle());
                                                    titleRewardTV.setTextColor(Color.parseColor(givenReward.getTitle().getColor()));
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }, 700);
            }
            bdouble_change = false;
        }
    }

    public void takeVisibility(ImageView v1, ImageView v2, ImageView v3, ImageView v4,
                               ImageView r1, ImageView r2, ImageView r3, ImageView r4) {
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);
        r1.setVisibility(View.GONE);
        r2.setVisibility(View.GONE);
        r3.setVisibility(View.GONE);
        r4.setVisibility(View.GONE);
    }

    public void setVisibilityToAnswers(TextView t1, TextView t2, TextView t3, TextView t4,
                                       ImageView i1, ImageView i2, ImageView i3, ImageView i4) {
        i1.setVisibility(View.VISIBLE);
        i2.setVisibility(View.VISIBLE);
        i3.setVisibility(View.VISIBLE);
        i4.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
    }

    public void goToBackActivity() {
        Intent intent = new Intent(context, NivelSelectActivity.class);
        intent.putExtra("categorie", categorie);
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, i1, i2, i3, i4;
        TextView question, t1, t2, t3, t4;
        ImageView r1, r2, r3, r4, v1, v2, v3, v4;
        ViewGroup viewGroup;
        CardView double_change, swichq, cinzeci, corect;

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
            viewGroup = itemView.findViewById(R.id.maket_question_layout);
            double_change = itemView.findViewById(R.id.double_chance);
            swichq = itemView.findViewById(R.id.swichq);
            cinzeci = itemView.findViewById(R.id.cinzeci);
            corect = itemView.findViewById(R.id.corect);
        }
    }
}
