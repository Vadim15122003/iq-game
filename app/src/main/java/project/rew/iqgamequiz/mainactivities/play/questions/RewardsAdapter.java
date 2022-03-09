package project.rew.iqgamequiz.mainactivities.play.questions;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {

    List<GivenReward> givenRewards;

    public RewardsAdapter(List<GivenReward> givenRewards) {
        this.givenRewards = givenRewards;
        if (givenRewards != null)
            Collections.sort(this.givenRewards, new Comparator<GivenReward>() {
                @Override
                public int compare(GivenReward givenReward, GivenReward t1) {
                    return FirebaseUtils.getInt(givenReward.getPointsNedeed()) - FirebaseUtils.getInt(t1.getPointsNedeed());
                }
            });
    }

    @NonNull
    @Override
    public RewardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_reward, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.ViewHolder holder, int position) {
        GivenReward current = givenRewards.get(position);
        holder.conditions.setText(current.getPointsNedeed());
        if (current.isClaimed()) {
            holder.claimed.setVisibility(View.VISIBLE);
        } else {
            holder.claimed.setVisibility(View.INVISIBLE);
        }

        if (current.getRewardType() == RewardType.Title) {
            holder.title.setVisibility(View.VISIBLE);
            holder.titleLogo.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.title.setText(current.getTitle().getTitle());
            holder.title.setTextColor(Color.parseColor(current.getTitle().getColor()));
            Picasso.get().load(current.getTitle().getLogo()).into(holder.titleLogo);
        }
        if (current.getRewardType() == RewardType.ProfileImage) {
            holder.title.setVisibility(View.GONE);
            holder.titleLogo.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            Picasso.get().load(current.getProfileImage().getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if (givenRewards != null)
            return givenRewards.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView conditions, title;
        ImageView image, titleLogo, claimed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conditions = itemView.findViewById(R.id.number);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.profile_img);
            titleLogo = itemView.findViewById(R.id.titleLogo);
            claimed = itemView.findViewById(R.id.claimed);
        }
    }
}
