package project.rew.iqgamequiz.mainactivities.topglory;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class TopGloryAdapter extends RecyclerView.Adapter<TopGloryAdapter.ViewHolder> {

    List<TopGloryPerson> topGloryPersonList;

    public TopGloryAdapter(List<TopGloryPerson> topGloryPersonList) {
        this.topGloryPersonList = topGloryPersonList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            topGloryPersonList.sort(new Comparator<TopGloryPerson>() {
                @Override
                public int compare(TopGloryPerson topGloryPerson, TopGloryPerson t1) {
                    return FirebaseUtils.getInt(topGloryPerson.getPlace())-FirebaseUtils.getInt(t1.getPlace());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_top_glory, parent, false);

        return new TopGloryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopGloryAdapter.ViewHolder holder, int position) {
        if (topGloryPersonList != null) {
            TopGloryPerson topGloryPerson = topGloryPersonList.get(position);
            if (topGloryPerson != null) {
                if (topGloryPerson.getUserName() != null)
                    holder.userName.setText(topGloryPerson.getUserName());
                if (topGloryPerson.getPlace() != null)
                    holder.place.setText(topGloryPerson.getPlace());
                if (topGloryPerson.getGlory() != null)
                    holder.glory.setText(topGloryPerson.getGlory());
                if (topGloryPerson.getTitle() != null && topGloryPerson.getTitle().getTitle() != null)
                    holder.title.setText(topGloryPerson.getTitle().getTitle());
                if (topGloryPerson.getTitle() != null && topGloryPerson.getTitle().getColor() != null)
                    holder.title.setTextColor(Color.parseColor(topGloryPerson.getTitle().getColor()));
                if (topGloryPerson.getProfileImage() != null && topGloryPerson.getProfileImage().getImage() != null)
                    Picasso.get().load(topGloryPerson.getProfileImage().getImage()).into(holder.profImg);
                if (topGloryPerson.getTitle() != null && topGloryPerson.getTitle().getLogo() != null)
                    Picasso.get().load(topGloryPerson.getTitle().getLogo()).into(holder.titleLogo);
                if (topGloryPerson.getTitle() != null && topGloryPerson.getTitle().getImage() != null) {
                    holder.titleImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(topGloryPerson.getTitle().getImage()).into(holder.titleImg);
                } else holder.titleImg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return topGloryPersonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profImg, titleLogo, titleImg;
        TextView title, glory, place, userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profImg = itemView.findViewById(R.id.profile_img);
            titleLogo = itemView.findViewById(R.id.title_logo);
            titleImg = itemView.findViewById(R.id.title_image);
            title = itemView.findViewById(R.id.title);
            glory = itemView.findViewById(R.id.glory);
            place = itemView.findViewById(R.id.place);
            userName = itemView.findViewById(R.id.username);
        }
    }
}
