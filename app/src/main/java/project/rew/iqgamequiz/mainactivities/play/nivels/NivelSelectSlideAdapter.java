package project.rew.iqgamequiz.mainactivities.play.nivels;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.questions.items.GivenReward;
import project.rew.iqgamequiz.mainactivities.play.questions.QuestionsActivity;
import project.rew.iqgamequiz.mainactivities.play.questions.adapters.RewardsAdapter;

public class NivelSelectSlideAdapter extends RecyclerView.Adapter<NivelSelectSlideAdapter.ViewHolder> {

    private List<Nivel> nivels = new ArrayList<>();
    String categorie;
    DatabaseReference ref;
    Context context;
    String categorieId;
    Dialog neddsMore, details;
    ImageView image_ok, details_ok;
    RecyclerView recyclerView;
    RewardsAdapter adapter;
    List<GivenReward> givenRewards = new ArrayList<>();

    public NivelSelectSlideAdapter(List<Nivel> nivels, String categorie, String categorieId, Context context) {
        this.nivels = nivels;
        this.categorie = categorie;
        this.context = context;
        this.categorieId = categorieId;
        ref = FirebaseDatabase.getInstance().getReference().child("RO").child(categorie);
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
        image_ok = neddsMore.findViewById(R.id.imageView6);
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
        details_ok = details.findViewById(R.id.imageView8);
        recyclerView = details.findViewById(R.id.recyclerview);
        givenRewards.add(new GivenReward("Beginer", null, "40"));
        givenRewards.add(new GivenReward("Erou", null, "569"));
        givenRewards.add(new GivenReward("Legenda", null, "35"));
        givenRewards.add(new GivenReward("Armn", null, "89"));
        givenRewards.add(new GivenReward("Inteleotul", null, "354"));
        givenRewards.add(new GivenReward("Inginer", null, "123"));
        givenRewards.add(new GivenReward("Expert", null, "145"));
        adapter = new RewardsAdapter(givenRewards);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        details_ok.setOnClickListener(v -> {
            details.cancel();
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NivelSelectSlideAdapter.ViewHolder holder, int position) {
        Nivel holderNivel = nivels.get(position);
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
                    intent.putExtra("categorie", categorie);
                    intent.putExtra("categorieId", categorieId);
                    intent.putExtra("nivel", position);
                    context.startActivity(intent);
                });
            }

        holder.forNext.setOnClickListener(v -> {
            neddsMore.show();
        });

        holder.details.setOnClickListener(v -> {
            details.show();
        });
    }

    @Override
    public int getItemCount() {
        return nivels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, blocked;
        TextView title, curent, nedeed;
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
        }
    }
}