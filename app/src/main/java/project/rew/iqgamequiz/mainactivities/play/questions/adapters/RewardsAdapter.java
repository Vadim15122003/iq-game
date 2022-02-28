package project.rew.iqgamequiz.mainactivities.play.questions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.play.questions.items.GivenReward;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder>{

    List<GivenReward> givenRewards;

    public RewardsAdapter(List<GivenReward> givenRewards) {
        this.givenRewards = givenRewards;
    }

    @NonNull
    @Override
    public RewardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_reward_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.ViewHolder holder, int position) {

        GivenReward current = givenRewards.get(position);
        if (current.getImage()!=null)
        holder.image.setImageResource(Integer.parseInt(current.getImage()));
        holder.title.setText(current.getTitle());
        holder.conditions.setText(current.getNeededPoints()+" răspunsuri corecte:");
    }

    @Override
    public int getItemCount() {
        return givenRewards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView conditions,title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conditions = itemView.findViewById(R.id.conditions);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }
}
