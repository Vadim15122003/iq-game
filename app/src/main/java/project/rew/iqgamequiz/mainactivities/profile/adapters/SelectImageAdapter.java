package project.rew.iqgamequiz.mainactivities.profile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {
    List<ProfileImage> images;
    ImageView image, image1;

    public SelectImageAdapter(List<ProfileImage> images, ImageView image, ImageView image1) {
        this.images = images;
        this.image = image;
        this.image1 = image1;
    }

    @NonNull
    @Override
    public SelectImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_select_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectImageAdapter.ViewHolder holder, int position) {
        String currImage = images.get(position).getImage();
        Picasso.get().load(currImage).into(holder.imageView);
        if (images.get(position).getId().equals(FirebaseUtils.profileImage.getId()))
            holder.selected.setVisibility(View.VISIBLE);
        else holder.selected.setVisibility(View.GONE);

        holder.imageView.setOnClickListener(v -> {
            FirebaseUtils.profileImage=images.get(position);
            Picasso.get().load(images.get(position).getImage()).into(image);
            Picasso.get().load(images.get(position).getImage()).into(image1);
            FirebaseUtils.setSelectedProfileImage(images.get(position).getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_img_dialog);
            selected = itemView.findViewById(R.id.selected);
        }
    }
}
