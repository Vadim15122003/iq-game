package project.rew.iqgamequiz.mainactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.imageview.ShapeableImageView;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.playactivities.SelectGeneralKnowlage;

public class PlaySelectMode extends AppCompatActivity {
    ShapeableImageView generalKnowings,logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_select_mode);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        generalKnowings=findViewById(R.id.generalKnews);
        logic=findViewById(R.id.logic);

        generalKnowings.setOnClickListener(view -> {
            openActivity(SelectGeneralKnowlage.class);
        });
        logic.setOnClickListener(view -> {

        });
    }

    private void openActivity(Class<?> cls){
        Intent intent = new Intent(PlaySelectMode.this,cls);
        startActivity(intent);
    }
}