package project.rew.iqgamequiz.playactivities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import project.rew.iqgamequiz.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView title;
    CardView cardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.title);
        cardView = itemView.findViewById(R.id.view);
    }
}
