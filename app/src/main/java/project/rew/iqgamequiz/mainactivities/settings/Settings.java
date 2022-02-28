package project.rew.iqgamequiz.mainactivities.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import project.rew.iqgamequiz.account.LoginActivity;
import project.rew.iqgamequiz.R;

import static project.rew.iqgamequiz.account.LoginActivity.mAuth;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button logout=findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            sendUserToLoginActivity();
        });
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(Settings.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}