package com.ribal.cutline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private FirebaseUser firebaseUser;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser!=null){
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    fStore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String type = document.getString("type");
                                    if(type.equals("barber")) {
                                        startActivity(new Intent(getApplicationContext(), MainActivityBarber.class));
                                        finish();
                                    } else if (type.equals("user")) {
                                        startActivity(new Intent(getApplicationContext(), MainActivityUser.class));
                                        finish();
                                    }
                                }
                            }
                        }
                    });
                }else {
                    Intent login = new Intent(SplashScreen.this, Login.class);
                    startActivity(login);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}