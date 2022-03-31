package project.rew.iqgamequiz.mainactivities.friends.friendprofile.adapters;

import android.graphics.Color;
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
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class SeeTitleAdapter extends RecyclerView.Adapter<SeeTitleAdapter.ViewHolder> {
    List<Title> titles;

    public SeeTitleAdapter(List<Title> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public SeeTitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_select_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeTitleAdapter.ViewHolder holder, int position) {
        Title currTitle = titles.get(position);
        holder.title.setText(currTitle.getTitle());
        if (currTitle.getColor() != null)
            holder.title.setTextColor(Color.parseColor(currTitle.getColor()));
        Picasso.get().load(titles.get(position).getLogo()).into(holder.titleLogo);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        ImageView titleLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.title);
            titleLogo = itemView.findViewById(R.id.titleLogo);
        }
    }
}
