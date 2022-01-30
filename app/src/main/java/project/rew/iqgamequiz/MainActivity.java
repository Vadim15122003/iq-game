package project.rew.iqgamequiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static project.rew.iqgamequiz.LoginActivity.mAuth;
import static project.rew.iqgamequiz.LoginActivity.mUser;
import static project.rew.iqgamequiz.RegisterActivity.fstore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (mAuth.getCurrentUser() == null)
            sendUserToLoginActivity();

        Button btn = findViewById(R.id.button);
        Button btn1 = findViewById(R.id.button3);
        TextView coins, glory;
        coins = findViewById(R.id.coins);
        glory = findViewById(R.id.glory);
        fstore = FirebaseFirestore.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            DocumentReference documentReference = fstore.collection("users").document(mAuth.getCurrentUser().getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            coins.setText(value.get("IqCoins").toString());
                            glory.setText(value.get("glory").toString());
                        }
                    }
            });
        }

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
}