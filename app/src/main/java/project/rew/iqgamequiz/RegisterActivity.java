package project.rew.iqgamequiz;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static project.rew.iqgamequiz.LoginActivity.mAuth;

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
                } else if (userName.length()<4) {
                    inputUsername.setError("Enter a userName wich minim 4 charcaters");
                } else if (userName.contains(" ")) {
                    inputUsername.setError("Enter a userName wichout space");
                } else if (!password.equals(confirmPassword)) {
                    inputConfirmPassword.setError("Paswseord is different");
                } else {
                    progressDialog.setMessage("Register account...");
                    progressDialog.setTitle("Register");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                DocumentReference documentReference = fstore.collection("users").document(email);
                                Map<String, Object> user = new HashMap<>();
                                user.put("username",userName);
                                user.put("IqCoins", 0);
                                user.put("glory", 0);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });

    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}