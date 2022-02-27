package project.rew.iqgamequiz.mainactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.ProfileImage;
import project.rew.iqgamequiz.Question;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.RegisterActivity;
import project.rew.iqgamequiz.SelectImageAdapter;
import project.rew.iqgamequiz.utils.FirebaseUtils;

import static project.rew.iqgamequiz.LoginActivity.mAuth;

public class Profile extends AppCompatActivity {

    TextView coins, glory, username, title;
    ImageView profile_img,profile_img_select;
    EditText newUserName;
    Button updateUserName;
    FirebaseFirestore fstore;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    Dialog selectProfileImage;
    CardView selectPICardView;
    RecyclerView recyclerView;
    SelectImageAdapter adapter;
    List<String> imagesId = new ArrayList<>();
    List<ProfileImage> images =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coins = findViewById(R.id.iq_coins);
        glory = findViewById(R.id.glory);
        username = findViewById(R.id.username);
        title = findViewById(R.id.title);
        profile_img = findViewById(R.id.profile_img);
        newUserName = findViewById(R.id.inputUsername);
        updateUserName = findViewById(R.id.updateUserName);
        selectPICardView = findViewById(R.id.cardView_select);
        profile_img_select=findViewById(R.id.profile_img_select);
        fstore = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("RO");
        progressDialog = new ProgressDialog(this);

        selectProfileImage = new Dialog(Profile.this, R.style.DialogTransparentBg);
        selectProfileImage.setContentView(R.layout.dialog_select_profile_image);
        selectProfileImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectProfileImage.setCancelable(true);
        WindowManager.LayoutParams lp = selectProfileImage.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        selectProfileImage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recyclerView = selectProfileImage.findViewById(R.id.recyclerview);

        coins.setText(FirebaseUtils.coins);
        glory.setText(FirebaseUtils.glory);
        username.setText(FirebaseUtils.username);
        newUserName.setText(FirebaseUtils.username);
        Picasso.get().load(FirebaseUtils.image).into(profile_img);
        Picasso.get().load(FirebaseUtils.image).into(profile_img_select);

        selectPICardView.setOnClickListener(v -> {
            selectProfileImage.show();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        fstore.collection("users").document(FirebaseUtils.email)
                .collection("images").document("images").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            imagesId = (List<String>) documentSnapshot.get("id");
                            for (String imageId : imagesId) {
                                ref.child("Images").child(imageId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        images.add(new ProfileImage(imageId,snapshot.getValue().toString()));
                                        adapter = new SelectImageAdapter(images, profile_img, profile_img_select);
                                        recyclerView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                });

        updateUserName.setOnClickListener(v -> {
            String newuserName = newUserName.getText().toString();
            if (newuserName.length() < 4) {
                newUserName.setError("Enter a userName wich minim 4 charcaters");
            } else if (newuserName.length() > 10) {
                newUserName.setError("Enter a userName wich maxim 10 charcaters");
            } else if (newuserName.contains(" ")) {
                newUserName.setError("Enter a userName wichout space");
            } else {
                progressDialog.setMessage("Updating username...");
                progressDialog.setTitle("Update");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                fstore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean exist = false;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String username = documentSnapshot.get("username").toString();
                                    if (username.equals(newuserName)) {
                                        exist = true;
                                        progressDialog.dismiss();
                                        newUserName.setError("A person wich this userName already exist");
                                    }
                                }
                            }
                            if (!exist) {
                                fstore.collection("users").document(FirebaseUtils.email).update("username", newuserName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseUtils.username = newuserName;
                                        username.setText(FirebaseUtils.username);
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Profile.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }
}