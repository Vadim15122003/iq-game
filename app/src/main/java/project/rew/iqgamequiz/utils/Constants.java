package project.rew.iqgamequiz.utils;


import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Constants extends AppCompatActivity {
    public static String coins,glory,username,title,email;
    static FirebaseFirestore fstore;

    public static void addCoins(int x, TextView tcoins){
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(email).update("IqCoins",x).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (tcoins!=null)
                tcoins.setText(Constants.coins);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void addGlory(int x, TextView tglory){
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(email).update("glory",x).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(tglory!=null)
                tglory.setText(Constants.glory);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static int getInt(String toConvert){
        return Integer.parseInt(toConvert.replaceAll("[^0-9.]",""));
    }
}
