package project.rew.iqgamequiz.mainactivities.friends.friendprofile.adapters;

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

public class SeeImageAdapter extends RecyclerView.Adapter<SeeImageAdapter.ViewHolder> {
    List<ProfileImage> images;

    public SeeImageAdapter(List<ProfileImage> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public SeeImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_select_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeImageAdapter.ViewHolder holder, int position) {
        String currImage = images.get(position).getImage();
        Picasso.get().load(currImage).into(holder.imageView);
        holder.selected.setVisibility(View.GONE);
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
