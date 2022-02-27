package project.rew.iqgamequiz.playactivities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.R;

public class SelectGeneralKnowlageAdapter extends RecyclerView.Adapter<SelectGeneralKnowlageAdapter.ViewHolder> {

    List<String> categories;
    DatabaseReference databaseReference;

    public SelectGeneralKnowlageAdapter(List<String> categories) {
        this.categories = categories;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Categories");
    }

    @NonNull
    @Override
    public SelectGeneralKnowlageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maket_categoire, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectGeneralKnowlageAdapter.ViewHolder holder, int position) {
        databaseReference.child(categories.get(position)).child("title").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    holder.title.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        databaseReference.child(categories.get(position)).child("image").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(String.valueOf(task.getResult().getValue())).into(holder.image);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
