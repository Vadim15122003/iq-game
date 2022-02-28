package project.rew.iqgamequiz.utils;


import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import project.rew.iqgamequiz.mainactivities.play.nivels.Nivel;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class FirebaseUtils extends AppCompatActivity {
    public static String coins, glory, username, email;
    public static ProfileImage profileImage;
    public static Title title = new Title();
    static FirebaseFirestore fstore;

    public static void addCoins(int x, TextView tcoins) {
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(email).update("IqCoins", x).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (tcoins != null)
                    tcoins.setText(FirebaseUtils.coins);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void addGlory(int x, TextView tglory) {
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(email).update("glory", x).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (tglory != null)
                    tglory.setText(FirebaseUtils.glory);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void getCurentCorectAnswers(String categoryId, String nivelId, Nivel nivel) {
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(email)
                .collection("resolved").document(categoryId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot.get(nivelId) != null) {
                    nivel.setCurent(String.valueOf(documentSnapshot.get(nivelId)));
                } else
                    nivel.setCurent("0");
            }
        });
    }

    public static void setCorectAnswers(String categoryId, String nivelId, int newCorectAnswers) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("resolved").document(categoryId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentReference.update(nivelId,
                                Integer.parseInt(String.valueOf(document.get(nivelId))) + newCorectAnswers);
                    } else {
                        Map<String, Object> nivel = new HashMap<>();
                        nivel.put(nivelId, newCorectAnswers);
                        documentReference.set(nivel);
                    }
                }
            }
        });
    }

    public static int getInt(String toConvert) {
        return Integer.parseInt(toConvert.replaceAll("[^0-9.]", ""));
    }

    public static void setSelectedProfileImage(String id) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("images").document("selected");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentReference.update("id", id);
                    } else {
                        Map<String, Object> nivel = new HashMap<>();
                        nivel.put("id", id);
                        documentReference.set(nivel);
                    }
                }
            }
        });
    }

    public static void setSelectedTitleImage(String id) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("titles").document("selected");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentReference.update("id", id);
                    } else {
                        Map<String, Object> nivel = new HashMap<>();
                        nivel.put("id", id);
                        documentReference.set(nivel);
                    }
                }
            }
        });
    }
}
