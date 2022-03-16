package project.rew.iqgamequiz.mainactivities.play.nivels.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.GeneralAtributes;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.nivels.items.Nivel;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.items.NivelAtributes;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class NivelSelectSlideAdapter extends RecyclerView.Adapter<NivelSelectSlideAdapter.ViewHolder> {

    private List<Nivel> nivels = new ArrayList<>();
    String categorie;
    DatabaseReference ref;
    Context context;
    String categorieId;
    Dialog neddsMore, details;
    ImageView image_ok;
    RecyclerView recyclerView;
    RewardsAdapter adapter;
    GeneralAtributes generalAtributes;
    TextView conditions, actualPoints, rewards_column, cond_coluumn, text_needMore;
    CardView details_dialog;
    ConstraintLayout dialog_need_more_lay;

    public NivelSelectSlideAdapter(List<Nivel> nivels, String categorie, String categorieId, Context context,
                                   GeneralAtributes generalAtributes) {
        this.nivels = nivels;
        this.categorie = categorie;
        this.context = context;
        this.categorieId = categorieId;
        this.generalAtributes = generalAtributes;
        ref = FirebaseDatabase.getInstance().getReference().child("RO").child("Categories").child(categorie);
    }

    @NonNull
    @Override
    public NivelSelectSlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_nivel_select, parent, false);
        neddsMore = new Dialog(context, R.style.DialogTransparentBg);
        neddsMore.setContentView(R.layout.dialog_points_for_next);
        neddsMore.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        neddsMore.setCancelable(true);
        WindowManager.LayoutParams lp = neddsMore.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        neddsMore.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        image_ok = neddsMore.findViewById(R.id.ok);
        image_ok.setOnClickListener(v -> {
            neddsMore.cancel();
        });

        details = new Dialog(context, R.style.DialogTransparentBg);
        details.setContentView(R.layout.dialog_rewards);
        details.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        details.setCancelable(true);
        WindowManager.LayoutParams lp1 = details.getWindow().getAttributes();
        lp1.dimAmount = 0.8f;
        details.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recyclerView = details.findViewById(R.id.recyclerview);

        conditions = details.findViewById(R.id.condition);
        actualPoints = details.findViewById(R.id.txt_actual_pts);
        rewards_column = details.findViewById(R.id.corect_colon);
        cond_coluumn = details.findViewById(R.id.rewards_colon);
        details_dialog = details.findViewById(R.id.dialog_details_lay);
        dialog_need_more_lay = neddsMore.findViewById(R.id.dialog_need_more_lay);
        text_needMore = neddsMore.findViewById(R.id.text);

        if (generalAtributes != null && generalAtributes.getDialog_txt_color() != null) {
            conditions.setTextColor(Color.parseColor(generalAtributes.getDialog_txt_color()));
            actualPoints.setTextColor(Color.parseColor(generalAtributes.getDialog_txt_color()));
            rewards_column.setTextColor(Color.parseColor(generalAtributes.getDialog_txt_color()));
            cond_coluumn.setTextColor(Color.parseColor(generalAtributes.getDialog_txt_color()));
        }
        if (generalAtributes != null && generalAtributes.getDialog_details_bckg() != null) {
            details_dialog.setCardBackgroundColor(Color.parseColor(generalAtributes.getDialog_details_bckg()));
        }
        if (generalAtributes != null && generalAtributes.getDialog_locked_txt_color() != null) {
            text_needMore.setTextColor(Color.parseColor(generalAtributes.getDialog_locked_txt_color()));
        }
        if (generalAtributes != null && generalAtributes.getDialog_lockeg_img() != null) {
            Picasso.get().load(generalAtributes.getDialog_lockeg_img()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    dialog_need_more_lay.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        if (generalAtributes != null && generalAtributes.getLocked_ok_img() != null) {
            Picasso.get().load(generalAtributes.getLocked_ok_img()).into(image_ok);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NivelSelectSlideAdapter.ViewHolder holder, int position) {
        Nivel holderNivel = nivels.get(position);
        NivelAtributes nivelAtributes = holderNivel.getNivelAtributes();
        holder.title.setText(holderNivel.getTitle());
        Picasso.get().load(holderNivel.getImage()).into(holder.image);
        holder.curent.setText(holderNivel.getCurent());
        holder.nedeed.setText(holderNivel.getNedeed());

        if (!holder.curent.getText().toString().equals("") && !holder.nedeed.getText().toString().equals(""))
            if (Integer.parseInt(holder.curent.getText().toString()) < Integer.parseInt(holder.nedeed.getText().toString())) {
                holder.blocked.setVisibility(View.VISIBLE);
                holder.forNext.setVisibility(View.VISIBLE);
            } else {
                holder.blocked.setVisibility(View.GONE);
                holder.forNext.setVisibility(View.GONE);
                holder.image.setOnClickListener(view -> {
                    Intent intent = new Intent(context, QuestionsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("categorie", categorie);
                    bundle.putString("categorieId", categorieId);
                    bundle.putInt("nivel", position);
                    bundle.putParcelable("general_atributes", generalAtributes);
                    bundle.putParcelable("nivel_atributes", nivelAtributes);
                    intent.putExtra("Bundles", bundle);
                    if (holderNivel.getGivenRewards() != null)
                        Collections.sort(holderNivel.getGivenRewards(), new Comparator<GivenReward>() {
                            @Override
                            public int compare(GivenReward givenReward, GivenReward t1) {
                                return FirebaseUtils.getInt(givenReward.getPointsNedeed()) - FirebaseUtils.getInt(t1.getPointsNedeed());
                            }
                        });
                    QuestionsActivity.givenRewards = holderNivel.getGivenRewards();
                    context.startActivity(intent);
                });
            }

        holder.forNext.setOnClickListener(v -> {
            neddsMore.show();
        });

        holder.details.setOnClickListener(v -> {
            adapter = new RewardsAdapter(holderNivel.getGivenRewards(), generalAtributes);
            recyclerView.setAdapter(adapter);
            if (holderNivel.getNivelAtributes() != null && holderNivel.getNivelAtributes().getIncorect_permision() != null)
                conditions.setText("Se permit maxim " + holderNivel.getNivelAtributes().getIncorect_permision() + " greșeli");
            actualPoints.setText("Răspunsuri corecte actuale: " + holderNivel.getCurentOfThis());
            details.show();
        });

    }

    @Override
    public int getItemCount() {
        return nivels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, blocked;
        TextView title, curent, nedeed, details_txt;
        LinearLayout forNext;
        CardView details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            blocked = itemView.findViewById(R.id.blocked);
            title = itemView.findViewById(R.id.title);
            forNext = itemView.findViewById(R.id.forNext);
            curent = itemView.findViewById(R.id.curent);
            nedeed = itemView.findViewById(R.id.needed);
            details = itemView.findViewById(R.id.details);
            details_txt = itemView.findViewById(R.id.txt_details);

            if (generalAtributes != null && generalAtributes.getDetails_bckg() != null)
                details.setCardBackgroundColor(Color.parseColor(generalAtributes.getDetails_bckg()));
            if (generalAtributes != null && generalAtributes.getDetails_txt_color() != null)
                details_txt.setTextColor(Color.parseColor(generalAtributes.getDetails_txt_color()));
            if (generalAtributes != null && generalAtributes.getTitle_color() != null)
                title.setTextColor(Color.parseColor(generalAtributes.getTitle_color()));
            if (generalAtributes != null && generalAtributes.getLocked_img() != null)
                Picasso.get().load(generalAtributes.getLocked_img()).into(blocked);
        }
    }
}
