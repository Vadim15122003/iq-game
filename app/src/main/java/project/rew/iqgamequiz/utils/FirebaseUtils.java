package project.rew.iqgamequiz.utils;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import project.rew.iqgamequiz.mainactivities.play.nivels.items.Nivel;
import project.rew.iqgamequiz.mainactivities.profile.items.ProfileImage;
import project.rew.iqgamequiz.mainactivities.profile.items.Title;

public class FirebaseUtils extends AppCompatActivity {
    public static String coins, glory, username, email;
    public static int newFriends;
    public static ProfileImage profileImage;
    public static Title title = new Title();
    public static List<String> friendsEmails = new ArrayList<>();
    public static List<String> pendingFriends = new ArrayList<>();
    public static List<String> inviteFriends = new ArrayList<>();
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
                        if (document.get("id") != null) {
                            List<String> curently_images = ((List<String>) document.get("id"));
                            boolean exist = false;
                            if (curently_images != null) {
                                for (String id_i : curently_images)
                                    if (id_i.equals(id)) {
                                        exist = true;
                                        break;
                                    }
                            }
                            if (!exist)
                                curently_images.add(id);
                            documentReference.update("id", curently_images);
                        } else {
                            List<String> curently_images = new ArrayList<>();
                            curently_images.add(id);
                            Map<String, Object> images_add = new HashMap<>();
                            images_add.put("id", curently_images);
                            documentReference.set(images_add);
                        }
                    } else {
                        List<String> curently_images = new ArrayList<>();
                        curently_images.add(id);
                        Map<String, Object> images_add = new HashMap<>();
                        images_add.put("id", curently_images);
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
                        if (document.get("id") != null) {
                            List<String> curently_titles = ((List<String>) document.get("id"));
                            boolean exist = false;
                            if (curently_titles != null) {
                                for (String id_t : curently_titles)
                                    if (id_t.equals(id)) {
                                        exist = true;
                                        break;
                                    }
                            }
                            if (!exist)
                                curently_titles.add(id);
                            documentReference.update("id", curently_titles);
                        } else {
                            List<String> curently_titles = new ArrayList<>();
                            curently_titles.add(id);
                            Map<String, Object> titles_add = new HashMap<>();
                            titles_add.put("id", curently_titles);
                            documentReference.set(titles_add);
                        }
                    } else {
                        List<String> curently_titles = new ArrayList<>();
                        curently_titles.add(id);
                        Map<String, Object> titles_add = new HashMap<>();
                        titles_add.put("id", curently_titles);
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
                        if (document.get(nivel) != null) {
                            List<String> rewards_completed = ((List<String>) document.get(nivel));
                            rewards_completed.add(id);
                            documentReference.update(nivel, rewards_completed);
                        } else {
                            List<String> rewards_completed = new ArrayList<>();
                            rewards_completed.add(id);
                            Map<String, Object> rewards_add = new HashMap<>();
                            rewards_add.put(nivel, rewards_completed);
                            documentReference.set(rewards_add);
                        }
                    } else {
                        List<String> rewards_completed = new ArrayList<>();
                        rewards_completed.add(id);
                        Map<String, Object> rewards_add = new HashMap<>();
                        rewards_add.put(nivel, rewards_completed);
                        documentReference.set(rewards_add);
                    }
                }
            }
        });
    }

    public static void verifyIfTopG(int glor, int i) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("TopGlory").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                if (!snapshot.child(String.valueOf(i)).exists() && i != 1)
                    verifyIfTopG(glor, i - 1);
                else if (!snapshot.child(String.valueOf(i)).exists() && i == 1)
                    ref.child("TopGlory").child(String.valueOf(i)).setValue(email);
                else {
                    String currEmail = snapshot.child(String.valueOf(i)).getValue().toString();
                    fstore.collection("users").document(Objects.requireNonNull(currEmail)).addSnapshotListener((value, error) -> {
                        if (value != null) {
                            if (value.exists()) {
                                if (glor > getInt(String.valueOf(value.get("glory")))) {
                                    ref.child("TopGlory").child(String.valueOf(i)).setValue(email);
                                    if (i < 100)
                                        ref.child("TopGlory").child(String.valueOf(i + 1)).setValue(currEmail);
                                    if (i > 1)
                                        verifyIfTopG(glor, i - 1);
                                } else if (i < 100 && !snapshot.child(String.valueOf(i + 1)).exists()) {
                                    ref.child("TopGlory").child(String.valueOf(i + 1)).setValue(email);
                                } else if (i > 1) {
                                    verifyIfTopG(glor, i - 1);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public static void verifyIfTopGFirstStep(int glor, int i) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("TopGlory").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                if (!snapshot.child(String.valueOf(i)).exists())
                    verifyIfTopGFirstStep(glor, i - 1);
                else if (i == 1 && !snapshot.child(String.valueOf(i)).getValue().toString().equals(email))
                    verifyIfTopG(glor, 100);
                else if (i > 1) {
                    String currEmail = snapshot.child(String.valueOf(i)).getValue().toString();
                    if (currEmail.equals(email)) verifyIfTopG(glor, i - 1);
                    else verifyIfTopGFirstStep(glor, i - 1);
                }
            }
        });
    }

    public static void inviteFriend(String friendEmail) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("pending") != null) {
                            List<String> pendings = ((List<String>) document.get("pending"));
                            pendings.add(friendEmail);
                            documentReference.update("pending", pendings);
                        } else {
                            List<String> pendings = new ArrayList<>();
                            pendings.add(friendEmail);
                            if (document.exists())
                                documentReference.update("pending", pendings);
                            else {
                                Map<String, Object> pendings_add = new HashMap<>();
                                pendings_add.put("pending", pendings);
                                documentReference.set(pendings_add);
                            }
                        }
                    } else {
                        List<String> pendings = new ArrayList<>();
                        pendings.add(friendEmail);
                        if (document.exists())
                            documentReference.update("pending", pendings);
                        else {
                            Map<String, Object> pendings_add = new HashMap<>();
                            pendings_add.put("pending", pendings);
                            documentReference.set(pendings_add);
                        }
                    }
                }
            }
        });
        DocumentReference documentReference1 = fstore.collection("users").document(friendEmail)
                .collection("friends").document("friends");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("inviting") != null) {
                            List<String> invitings = ((List<String>) document.get("inviting"));
                            invitings.add(email);
                            documentReference1.update("inviting", invitings);
                        } else {
                            List<String> invitings = new ArrayList<>();
                            invitings.add(email);
                            if (document.exists())
                                documentReference1.update("inviting", invitings);
                            else {
                                Map<String, Object> invitings_add = new HashMap<>();
                                invitings_add.put("inviting", invitings);
                                documentReference1.set(invitings_add);
                            }
                        }
                    } else {
                        List<String> invitings = new ArrayList<>();
                        invitings.add(email);
                        if (document.exists())
                            documentReference1.update("inviting", invitings);
                        else {
                            Map<String, Object> invitings_add = new HashMap<>();
                            invitings_add.put("inviting", invitings);
                            documentReference1.set(invitings_add);
                        }
                    }
                }
            }
        });
        if (FirebaseUtils.pendingFriends != null)
            FirebaseUtils.pendingFriends.add(friendEmail);
        else {
            FirebaseUtils.pendingFriends = new ArrayList<>();
            FirebaseUtils.pendingFriends.add(friendEmail);
        }
    }

    public static void acceptFriend(String friendEmail) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("inviting") != null) {
                            List<String> invitings = ((List<String>) document.get("inviting"));
                            for (int i = 0; i < invitings.size(); i++) {
                                String invited = invitings.get(i);
                                if (invited.equals(friendEmail))
                                    invitings.remove(i);
                            }
                            documentReference.update("inviting", invitings);
                        }

                        if (document.get("actual_friends") != null) {
                            List<String> actual_friends = ((List<String>) document.get("actual_friends"));
                            actual_friends.add(friendEmail);
                            documentReference.update("actual_friends", actual_friends);
                        } else {
                            List<String> actual_friends = new ArrayList<>();
                            actual_friends.add(friendEmail);
                            if (document.exists())
                                documentReference.update("actual_friends", actual_friends);
                            else {
                                Map<String, Object> actual_friends_add = new HashMap<>();
                                actual_friends_add.put("actual_friends", actual_friends);
                                documentReference.set(actual_friends_add);
                            }
                        }

                    } else {
                        List<String> actual_friends = new ArrayList<>();
                        actual_friends.add(friendEmail);
                        if (document.exists())
                            documentReference.update("actual_friends", actual_friends);
                        else {
                            Map<String, Object> actual_friends_add = new HashMap<>();
                            actual_friends_add.put("actual_friends", actual_friends);
                            documentReference.set(actual_friends_add);
                        }
                    }
                }
            }
        });
        DocumentReference documentReference1 = fstore.collection("users").document(friendEmail)
                .collection("friends").document("friends");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("pending") != null) {
                            List<String> pendings = ((List<String>) document.get("pending"));
                            for (int i = 0; i < pendings.size(); i++) {
                                String pending = pendings.get(i);
                                if (pending.equals(email))
                                    pendings.remove(i);
                            }
                            documentReference1.update("pending", pendings);
                        }
                        if (document.get("actual_friends") != null) {
                            List<String> actual_friends = ((List<String>) document.get("actual_friends"));
                            actual_friends.add(email);
                            documentReference1.update("actual_friends", actual_friends);
                        } else {
                            List<String> actual_friends = new ArrayList<>();
                            actual_friends.add(email);
                            if (document.exists())
                                documentReference1.update("actual_friends", actual_friends);
                            else {
                                Map<String, Object> actual_friends_add = new HashMap<>();
                                actual_friends_add.put("actual_friends", actual_friends);
                                documentReference1.set(actual_friends_add);
                            }
                        }
                        if (document.get("newFriends") != null) {
                            int i = getInt(document.get("newFriends").toString());
                            documentReference1.update("newFriends", i + 1);
                        } else {
                            int i = 1;
                            if (document.exists())
                                documentReference1.update("newFriends", i);
                            else {
                                Map<String, Object> newFriends = new HashMap<>();
                                newFriends.put("newFriends", i);
                                documentReference1.set(newFriends);
                            }
                        }
                    } else {
                        List<String> actual_friends = new ArrayList<>();
                        actual_friends.add(email);
                        if (document.exists())
                            documentReference1.update("actual_friends", actual_friends);
                        else {
                            Map<String, Object> actual_friends_add = new HashMap<>();
                            actual_friends_add.put("actual_friends", actual_friends);
                            documentReference1.set(actual_friends_add);
                        }
                    }
                }
            }
        });

        for (int i = 0; i < FirebaseUtils.inviteFriends.size(); i++) {
            if (FirebaseUtils.inviteFriends.get(i).equals(friendEmail))
                FirebaseUtils.inviteFriends.remove(i);
        }
        if (FirebaseUtils.friendsEmails != null)
            FirebaseUtils.friendsEmails.add(friendEmail);
        else {
            FirebaseUtils.friendsEmails = new ArrayList<>();
            FirebaseUtils.friendsEmails.add(friendEmail);
        }
    }

    public static void setNewFriendsZero() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("newFriends") != null) {
                            int i = 0;
                            documentReference.update("newFriends", i);
                        } else {
                            int i = 0;
                            if (document.exists())
                                documentReference.update("newFriends", i);
                            else {
                                Map<String, Object> newFriends = new HashMap<>();
                                newFriends.put("newFriends", i);
                                documentReference.set(newFriends);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void declineFriend(String friendEmail) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("inviting") != null) {
                            List<String> invitings = ((List<String>) document.get("inviting"));
                            for (int i = 0; i < invitings.size(); i++) {
                                String invited = invitings.get(i);
                                if (invited.equals(friendEmail))
                                    invitings.remove(i);
                            }
                            documentReference.update("inviting", invitings);
                        }

                    }
                }
            }
        });
        DocumentReference documentReference1 = fstore.collection("users").document(friendEmail)
                .collection("friends").document("friends");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("pending") != null) {
                            List<String> pendings = ((List<String>) document.get("pending"));
                            for (int i = 0; i < pendings.size(); i++) {
                                String pending = pendings.get(i);
                                if (pending.equals(email))
                                    pendings.remove(i);
                            }
                            documentReference1.update("pending", pendings);
                        }
                    }
                }
            }
        });

        for (int i = 0; i < FirebaseUtils.inviteFriends.size(); i++) {
            if (FirebaseUtils.inviteFriends.get(i).equals(friendEmail))
                FirebaseUtils.inviteFriends.remove(i);
        }
    }

    public static void deleteFriend(String friendEmail) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("actual_friends") != null) {
                            List<String> actual_friends = ((List<String>) document.get("actual_friends"));
                            for (int i = 0; i < actual_friends.size(); i++) {
                                String friend = actual_friends.get(i);
                                if (friend.equals(friendEmail))
                                    actual_friends.remove(i);
                            }
                            documentReference.update("actual_friends", actual_friends);
                        }
                    }
                }
            }
        });
        DocumentReference documentReference1 = fstore.collection("users").document(friendEmail)
                .collection("friends").document("friends");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("actual_friends") != null) {
                            List<String> actual_friends = ((List<String>) document.get("actual_friends"));
                            for (int i = 0; i < actual_friends.size(); i++) {
                                String friend = actual_friends.get(i);
                                if (friend.equals(email))
                                    actual_friends.remove(i);
                            }
                            documentReference1.update("actual_friends", actual_friends);
                        }
                    }
                }
            }
        });

        for (int i = 0; i < FirebaseUtils.friendsEmails.size(); i++) {
            if (FirebaseUtils.friendsEmails.get(i).equals(friendEmail))
                FirebaseUtils.friendsEmails.remove(i);
        }
    }

    public static void cancelInvite(String friendEmail) {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("users").document(email)
                .collection("friends").document("friends");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("pending") != null) {
                            List<String> pendings = ((List<String>) document.get("pending"));
                            for (int i = 0; i < pendings.size(); i++) {
                                String pending = pendings.get(i);
                                if (pending.equals(friendEmail))
                                    pendings.remove(i);
                            }
                            documentReference.update("pending", pendings);
                        }
                    }
                }
            }
        });
        DocumentReference documentReference1 = fstore.collection("users").document(friendEmail)
                .collection("friends").document("friends");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("inviting") != null) {
                            List<String> invitings = ((List<String>) document.get("inviting"));
                            for (int i = 0; i < invitings.size(); i++) {
                                String inviting = invitings.get(i);
                                if (inviting.equals(email))
                                    invitings.remove(i);
                            }
                            documentReference1.update("inviting", invitings);
                        }
                    }
                }
            }
        });

        for (int i = 0; i < FirebaseUtils.pendingFriends.size(); i++) {
            if (FirebaseUtils.pendingFriends.get(i).equals(friendEmail))
                FirebaseUtils.pendingFriends.remove(i);
        }
    }
}
