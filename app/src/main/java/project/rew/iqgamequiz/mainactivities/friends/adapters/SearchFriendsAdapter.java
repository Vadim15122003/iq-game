package project.rew.iqgamequiz.mainactivities.friends.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.friends.enums.FriendType;
import project.rew.iqgamequiz.mainactivities.friends.items.Friend;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> {

    List<Friend> friends;

    public SearchFriendsAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_friend_add, parent, false);
        return new SearchFriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendsAdapter.ViewHolder holder, int position) {
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
                if (friend.getFriendType() != null) {
                    if (friend.getFriendType() == FriendType.Anonym) {
                        holder.addFriend.setText("Add");
                        holder.addFriend.setBackgroundColor(Color.parseColor("#FF3700B3"));
                        holder.addFriend.setOnClickListener(v -> {
                            FirebaseUtils.inviteFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Pending);
                            holder.addFriend.setText("Pending");
                            holder.addFriend.setBackgroundColor(Color.parseColor("#59605C"));
                            holder.addFriend.setClickable(false);
                        });
                    } else if (friend.getFriendType() == FriendType.Friend) {
                        holder.addFriend.setText("Friend");
                        holder.addFriend.setClickable(false);
                        holder.addFriend.setBackgroundColor(Color.parseColor("#047731"));
                    } else if (friend.getFriendType() == FriendType.Inviting) {
                        holder.addFriend.setText("Accept");
                        holder.addFriend.setBackgroundColor(Color.parseColor("#0D5FCC"));
                        holder.addFriend.setOnClickListener(v -> {
                            FirebaseUtils.acceptFriend(friend.getEmail());
                            friend.setFriendType(FriendType.Friend);
                            holder.addFriend.setText("Friend");
                            holder.addFriend.setClickable(false);
                            holder.addFriend.setBackgroundColor(Color.parseColor("#047731"));
                        });
                    } else if (friend.getFriendType() == FriendType.Pending) {
                        holder.addFriend.setText("Pending");
                        holder.addFriend.setBackgroundColor(Color.parseColor("#59605C"));
                        holder.addFriend.setClickable(false);
                    }
                }
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
        Button addFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profImg = itemView.findViewById(R.id.profile_img);
            titleImg = itemView.findViewById(R.id.title_image);
            titleLogo = itemView.findViewById(R.id.title_logo);
            userName = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.title);
            addFriend = itemView.findViewById(R.id.addFriend);
        }
    }
}
