package project.rew.iqgamequiz.mainactivities.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.rew.iqgamequiz.MainActivity;
import project.rew.iqgamequiz.R;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.adapters.SelectImageAdapter;
import project.rew.iqgamequiz.mainactivities.profile.adapters.SelectTitleAdapter;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;
import project.rew.iqgamequiz.utils.FirebaseUtils;

public class ProfileActivity extends AppCompatActivity {

    TextView coins, glory, username, title, title_select;
    ImageView profile_img, profile_img_select, title_logo, title_image, title_logo_select;
    EditText newUserName;
    Button updateUserName;
    FirebaseFirestore fstore;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    Dialog selectProfileImage, selectTitle;
    CardView selectPICardView, selectTCardView;
    RecyclerView recyclerView, recyclerViewT;
    SelectImageAdapter adapter;
    SelectTitleAdapter selectTitleAdapter;
    List<String> imagesId = new ArrayList<>();
    List<ProfileImage> images = new ArrayList<>();
    List<String> titlesId = new ArrayList<>();
    List<Title> titles = new ArrayList<>();

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
        profile_img_select = findViewById(R.id.profile_img_select);
        title_logo = findViewById(R.id.title_logo);
        title_image = findViewById(R.id.title_image);
        title_select = findViewById(R.id.title_select);
        title_logo_select = findViewById(R.id.title_logo_select);
        selectTCardView = findViewById(R.id.select_title);
        fstore = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("RO");
        progressDialog = new ProgressDialog(this);

        selectTitle = new Dialog(ProfileActivity.this, R.style.DialogTransparentBg);
        selectTitle.setContentView(R.layout.dialog_select_title);
        selectTitle.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectTitle.setCancelable(true);
        WindowManager.LayoutParams lp = selectTitle.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        selectTitle.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recyclerViewT = selectTitle.findViewById(R.id.recyclerview);

        selectProfileImage = new Dialog(ProfileActivity.this, R.style.DialogTransparentBg);
        selectProfileImage.setContentView(R.layout.dialog_select_profile_image);
        selectProfileImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectProfileImage.setCancelable(true);
        WindowManager.LayoutParams lp1 = selectProfileImage.getWindow().getAttributes();
        lp1.dimAmount = 0.7f;
        selectProfileImage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recyclerView = selectProfileImage.findViewById(R.id.recyclerview);

        coins.setText(FirebaseUtils.coins);
        glory.setText(FirebaseUtils.glory);
        username.setText(FirebaseUtils.username);
        newUserName.setText(FirebaseUtils.username);
        Picasso.get().load(FirebaseUtils.profileImage.getImage()).into(profile_img);
        Picasso.get().load(FirebaseUtils.profileImage.getImage()).into(profile_img_select);
        title.setText(FirebaseUtils.title.getTitle());
        title.setTextColor(Color.parseColor(FirebaseUtils.title.getColor()));
        title_select.setText(FirebaseUtils.title.getTitle());
        title_select.setTextColor(Color.parseColor(FirebaseUtils.title.getColor()));
        Picasso.get().load(FirebaseUtils.title.getLogo()).into(title_logo);
        Picasso.get().load(FirebaseUtils.title.getLogo()).into(title_logo_select);
        if (FirebaseUtils.title.getImage() != null) {
            title_image.setVisibility(View.VISIBLE);
            Picasso.get().load(FirebaseUtils.title.getImage()).into(title_image);
        } else title_image.setVisibility(View.GONE);

        selectPICardView.setOnClickListener(v -> {
            selectProfileImage.show();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerViewT.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewT.setHasFixedSize(true);

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
                                        images.add(new ProfileImage(imageId, snapshot.getValue().toString()));
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

        fstore.collection("users").document(FirebaseUtils.email)
                .collection("titles").document("titles").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            titlesId = (List<String>) documentSnapshot.get("id");
                            for (String titleId : titlesId) {
                                Title currentTitle = new Title();
                                currentTitle.setId(titleId);
                                ref.child("Titles").child(titleId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            currentTitle.setTitle(snapshot.child("title").getValue().toString());
                                            currentTitle.setColor(snapshot.child("color").getValue().toString());
                                            currentTitle.setLogo(snapshot.child("logo").getValue().toString());
                                            if (snapshot.child("image").exists())
                                                currentTitle.setImage(snapshot.child("image").getValue().toString());
                                            else currentTitle.setImage(null);
                                            titles.add(currentTitle);
                                            selectTitleAdapter = new SelectTitleAdapter(titles, title_select, title_logo_select,
                                                    title, title_logo, title_image);
                                            recyclerViewT.setAdapter(selectTitleAdapter);
                                        }
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
                                        Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        selectTCardView.setOnClickListener(v -> {
            selectTitle.show();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}