package project.rew.iqgamequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import project.rew.iqgamequiz.mainactivities.Friends;
import project.rew.iqgamequiz.mainactivities.PlaySelectMode;
import project.rew.iqgamequiz.mainactivities.Profile;
import project.rew.iqgamequiz.mainactivities.Settings;
import project.rew.iqgamequiz.mainactivities.TopGlory;
import project.rew.iqgamequiz.utils.Constants;

import static project.rew.iqgamequiz.LoginActivity.mAuth;

public class MainActivity extends AppCompatActivity {

    TextView coins, glory, username, title;
    ImageView profile_img, play, profile, friends, top_glory, settings;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fstore = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() == null)
            sendUserToLoginActivity();

        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        username = findViewById(R.id.username);
        title = findViewById(R.id.title);
        profile_img = findViewById(R.id.profile_img);
        play = findViewById(R.id.play);
        profile = findViewById(R.id.profile);
        friends = findViewById(R.id.friends);
        top_glory = findViewById(R.id.top_glory);
        settings = findViewById(R.id.settings);

        getData();

        play.setOnClickListener(view -> openActivity(PlaySelectMode.class));
        profile.setOnClickListener(view -> openActivity(Profile.class));
        friends.setOnClickListener(view -> openActivity(Friends.class));
        top_glory.setOnClickListener(view -> openActivity(TopGlory.class));
        settings.setOnClickListener(view -> openActivity(Settings.class));
        /*btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> newIqCoins = new HashMap<>();
                newIqCoins.put("IqCoins", 592);
                fstore.collection("users").whereEqualTo("glory", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentId=documentSnapshot.getId();
                            fstore.collection("users").document(documentId).update(newIqCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }
                });
            }
        });*/
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getData(){
        if (mAuth.getCurrentUser() != null) {
            DocumentReference documentReference = fstore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()));
            documentReference.addSnapshotListener((value, error) -> {
                if (value!=null) {
                    if (value.exists()) {
                        Constants.coins= Objects.requireNonNull(value.get("IqCoins")).toString()+" ";
                        Constants.glory= Objects.requireNonNull(value.get("glory")).toString()+" ";
                        Constants.username= Objects.requireNonNull(value.get("username")).toString();
                        coins.setText(Constants.coins);
                        glory.setText(Constants.glory);
                        username.setText(Constants.username);
                    }
                }
            });
        }
    }

    private void openActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this,cls);
        startActivity(intent);
    }

}