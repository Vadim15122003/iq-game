package project.rew.iqgamequiz;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.utils.FirebaseUtils;

public class SelectTitleAdapter extends RecyclerView.Adapter<SelectTitleAdapter.ViewHolder> {
    List<Title> titles;
    TextView title, title_select;
    ImageView title_logo, title_logo_select, title_image;

    public SelectTitleAdapter(List<Title> titles, TextView title_select, ImageView title_logo_select,
                              TextView title, ImageView title_logo, ImageView title_image) {
        this.titles = titles;
        this.title_select = title_select;
        this.title_logo_select = title_logo_select;
        this.title = title;
        this.title_logo = title_logo;
        this.title_image = title_image;
    }

    @NonNull
    @Override
    public SelectTitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_select_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTitleAdapter.ViewHolder holder, int position) {
        Title currTitle = titles.get(position);
        holder.title.setText(currTitle.getTitle());
        if (currTitle.getColor() != null)
            holder.title.setTextColor(Color.parseColor(currTitle.getColor()));
        Picasso.get().load(titles.get(position).getLogo()).into(holder.titleLogo);
        if (currTitle.getId().equals(FirebaseUtils.title.getId())) {
            holder.cardView.setBackgroundColor(Color.parseColor("#20ED6B"));
        } else holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.cardView.setOnClickListener(v -> {
            FirebaseUtils.setSelectedTitleImage(titles.get(position).getId());
            FirebaseUtils.title = titles.get(position);
            title.setText(titles.get(position).getTitle());
            Picasso.get().load(titles.get(position).getLogo()).into(title_logo);
            title_select.setText(titles.get(position).getTitle());
            Picasso.get().load(titles.get(position).getLogo()).into(title_logo_select);
            if (titles.get(position).getImage() != null) {
                title_image.setVisibility(View.VISIBLE);
                Picasso.get().load(titles.get(position).getImage()).into(title_image);
            } else title_image.setVisibility(View.GONE);
            notifyDataSetChanged();
        });

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
