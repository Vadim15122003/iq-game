package project.rew.iqgamequiz.mainactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.playactivities.SelectGeneralKnowlage;
import project.rew.iqgamequiz.utils.Constants;

public class PlaySelectMode extends AppCompatActivity {
    ShapeableImageView generalKnowings,logic;
    TextView coins,glory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_select_mode);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);

        glory.setText(Constants.glory);
        coins.setText(Constants.coins);
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