package project.rew.iqgamequiz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.Questions;
import project.rew.iqgamequiz.R;

public class NivelSelectSlideAdapter extends RecyclerView.Adapter<NivelSelectSlideAdapter.ViewHolder> {

    private List<String> nivels = new ArrayList<>();
    String categorie;
    DatabaseReference ref;
    Context context;

    public NivelSelectSlideAdapter(List<String> nivels, String categorie, Context context) {
        this.nivels = nivels;
        this.categorie = categorie;
        this.context = context;
        ref = FirebaseDatabase.getInstance().getReference().child("Categories").child(categorie).child("nivels");
    }

    @NonNull
    @Override
    public NivelSelectSlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_nivel_select, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NivelSelectSlideAdapter.ViewHolder holder, int position) {
        ref.child(nivels.get(position)).child("title").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    holder.title.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        ref.child(nivels.get(position)).child("image").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(String.valueOf(task.getResult().getValue())).into(holder.image);
                }
            }
        });
        holder.image.setOnClickListener(view -> {
            Intent intent = new Intent(context, Questions.class);
            intent.putExtra("categorie", categorie);
            intent.putExtra("nivel", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return nivels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, blocked;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            blocked = itemView.findViewById(R.id.blocked);
            title = itemView.findViewById(R.id.title);
        }
    }
}
