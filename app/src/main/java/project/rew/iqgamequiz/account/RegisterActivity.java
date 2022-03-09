package project.rew.iqgamequiz.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.R;

import static project.rew.iqgamequiz.account.LoginActivity.mAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText inputEmail, inputPasword, inputConfirmPassword, inputUsername;
    TextView login;
    Button bttnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";
    ProgressDialog progressDialog;
    public static FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = findViewById(R.id.alreadyHaveAccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputPasword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConformPassword);
        bttnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        inputUsername = findViewById(R.id.inputUsername);
        fstore = FirebaseFirestore.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        bttnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPasword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();
                String userName = inputUsername.getText().toString();

                if (email.matches(emailPattern)) {
                    inputEmail.setError("Enter a valid email");
                } else if (password.isEmpty() || password.length() < 6) {
                    inputPasword.setError("Enter a password wich minim 6 charcters");
                } else if (userName.length() < 4) {
                    inputUsername.setError("Enter a userName wich minim 4 charcaters");
                } else if (userName.length() > 10) {
                    inputUsername.setError("Enter a userName wich maxim 10 charcaters");
                } else if (userName.contains(" ")) {
                    inputUsername.setError("Enter a userName wichout space");
                } else if (!password.equals(confirmPassword)) {
                    inputConfirmPassword.setError("Paswseord is different");
                } else {
                    progressDialog.setMessage("Register account...");
                    progressDialog.setTitle("Register");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    fstore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean exist=false;
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    if (documentSnapshot.exists()) {
                                        String username = documentSnapshot.get("username").toString();
                                        if (username.equals(userName)) {
                                            exist=true;
                                            progressDialog.dismiss();
                                            inputUsername.setError("A person wich this userName already exist");
                                        }
                                    }
                                }
                                if (!exist)
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            DocumentReference documentReference = fstore.collection("users").document(email);
                                            DocumentReference documentReference1=fstore.collection("users").document(email)
                                                    .collection("images").document("selected");
                                            DocumentReference documentReference2=fstore.collection("users").document(email)
                                                    .collection("images").document("images");
                                            DocumentReference documentReference3=fstore.collection("users").document(email)
                                                    .collection("titles").document("selected");
                                            DocumentReference documentReference4=fstore.collection("users").document(email)
                                                    .collection("titles").document("titles");
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("username", userName);
                                            user.put("IqCoins", 0);
                                            user.put("glory", 0);
                                            Map<String,Object> selected=new HashMap<>();
                                            Map<String,Object> selected_t=new HashMap<>();
                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            selected.put("id","0");
                                            selected_t.put("id","0");
                                            documentReference1.set(selected).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            documentReference3.set(selected_t).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            Map<String,Object> images=new HashMap<>();
                                            List<String> ids=new ArrayList<>();
                                            ids.add("0");
                                            images.put("id",ids);
                                            Map<String,Object> titles=new HashMap<>();
                                            List<String> ids_t=new ArrayList<>();
                                            ids_t.add("0");
                                            titles.put("id",ids_t);
                                            documentReference2.set(images).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            documentReference4.set(titles).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            sendUserToLoginActivity();
                                            Toast.makeText(RegisterActivity.this, "Registratino succefuly", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}