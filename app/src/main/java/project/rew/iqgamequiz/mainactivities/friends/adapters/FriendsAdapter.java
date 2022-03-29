package project.rew.iqgamequiz.mainactivities.friends.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    List<Friend> friends;

    public FriendsAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_friend, parent, false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, int position) {
        if (friends != null) {
            Friend friend = friends.get(position);
            if (friend != null) {
                if (friend.getUsername() != null)
                    holder.userName.setText(friend.getUsername());
                if (friend.getTitle() != null && friend.getTitle().getTitle() != null)
                    holder.title.setText(friend.getTitle().getTitle());
                if (friend.getTitle() != null && friend.getTitle().getColor() != null)
                    holder.title.setTextColor(Color.parseColor(friend.getTitle().getColor()));
                if (friend.getProfileImage() != null && friend.getProfileImage().getImage() != null)
                    Picasso.get().load(friend.getProfileImage().getImage()).into(holder.profImg);
                if (friend.getTitle() != null && friend.getTitle().getLogo() != null)
                    Picasso.get().load(friend.getTitle().getLogo()).into(holder.titleLogo);
                if (friend.getTitle() != null && friend.getTitle().getImage() != null) {
                    holder.titleImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(friend.getTitle().getImage()).into(holder.titleImg);
                } else holder.titleImg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profImg, titleImg, titleLogo;
        TextView userName, title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profImg = itemView.findViewById(R.id.profile_img);
            titleImg = itemView.findViewById(R.id.title_image);
            titleLogo = itemView.findViewById(R.id.title_logo);
            userName = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.title);
        }
    }
}
