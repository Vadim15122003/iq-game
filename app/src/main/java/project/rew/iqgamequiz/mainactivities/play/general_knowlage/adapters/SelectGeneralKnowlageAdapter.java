package project.rew.iqgamequiz.mainactivities.play.general_knowlage.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.general_knowlage.items.KnewCategorie;
import project.rew.iqgamequiz.mainactivities.play.nivels.NivelSelectActivity;

public class SelectGeneralKnowlageAdapter extends RecyclerView.Adapter<SelectGeneralKnowlageAdapter.ViewHolder> {

    List<KnewCategorie> categories;
    Context context;

    public SelectGeneralKnowlageAdapter(List<KnewCategorie> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectGeneralKnowlageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maket_categoire, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectGeneralKnowlageAdapter.ViewHolder holder, int position) {
        KnewCategorie knewCategorie = categories.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NivelSelectActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("categorie", knewCategorie.getTitle());
                bundle.putParcelable("general_atributes",knewCategorie.getGeneral_atributes());
                intent.putExtra("Bundles",bundle);
                context.startActivity(intent);
            }
        });
        holder.title.setText(knewCategorie.getTitle());
        Picasso.get().load(knewCategorie.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            view = itemView.findViewById(R.id.view);
        }
    }
}
