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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.rew.iqgamequiz.mainactivities.play.nivels.items.Nivel;
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

    public static void addProfileImage(String id) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("images").document("images");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("id")!=null) {
                            List<String> curently_images = ((List<String>) document.get("id"));
                            boolean exist = false;
                            if (curently_images != null) {
                                for(String id_i:curently_images)
                                    if (id_i.equals(id)) {
                                        exist = true;
                                        break;
                                    }
                            }
                            if(!exist)
                            curently_images.add(id);
                            documentReference.update("id", curently_images);
                        } else{
                            List<String> curently_images=new ArrayList<>();
                            curently_images.add(id);
                            Map<String,Object> images_add=new HashMap<>();
                            images_add.put("id",curently_images);
                            documentReference.set(images_add);
                        }
                    } else {
                        List<String> curently_images=new ArrayList<>();
                        curently_images.add(id);
                        Map<String,Object> images_add=new HashMap<>();
                        images_add.put("id",curently_images);
                        documentReference.set(images_add);
                    }
                }
            }
        });
    }

    public static void addTitle(String id) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("titles").document("titles");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("id")!=null) {
                            List<String> curently_titles = ((List<String>) document.get("id"));
                            boolean exist = false;
                            if (curently_titles != null) {
                                for(String id_t:curently_titles)
                                    if (id_t.equals(id)) {
                                        exist = true;
                                        break;
                                    }
                            }
                            if(!exist)
                            curently_titles.add(id);
                            documentReference.update("id", curently_titles);
                        } else{
                            List<String> curently_titles=new ArrayList<>();
                            curently_titles.add(id);
                            Map<String,Object> titles_add=new HashMap<>();
                            titles_add.put("id",curently_titles);
                            documentReference.set(titles_add);
                        }
                    } else {
                        List<String> curently_titles=new ArrayList<>();
                        curently_titles.add(id);
                        Map<String,Object> titles_add=new HashMap<>();
                        titles_add.put("id",curently_titles);
                        documentReference.set(titles_add);
                    }
                }
            }
        });
    }

    public static void setRewardClaimed(String categorie, String nivel, String id) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("rewards_claimed").document(categorie);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get(nivel)!=null) {
                            List<String> rewards_completed = ((List<String>) document.get(nivel));
                            rewards_completed.add(id);
                            documentReference.update(nivel, rewards_completed);
                        } else{
                            List<String> rewards_completed=new ArrayList<>();
                            rewards_completed.add(id);
                            Map<String,Object> rewards_add=new HashMap<>();
                            rewards_add.put(nivel,rewards_completed);
                            documentReference.set(rewards_add);
                        }
                    } else {
                        List<String> rewards_completed=new ArrayList<>();
                        rewards_completed.add(id);
                        Map<String,Object> rewards_add=new HashMap<>();
                        rewards_add.put(nivel,rewards_completed);
                        documentReference.set(rewards_add);
                    }
                }
            }
        });
    }
}
