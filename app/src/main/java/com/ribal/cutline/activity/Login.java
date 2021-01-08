package com.ribal.cutline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ribal.cutline.R;

public class Login extends AppCompatActivity {

    EditText uEmail;
    EditText uPass;
    Button submit;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = findViewById(R.id.login_btn);
        uEmail = findViewById(R.id.email_et);
        uPass = findViewById(R.id.pass_et);
        registerTv = findViewById(R.id.register_tv);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = uEmail.getText().toString().trim();
                final String password = uPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    uEmail.setError("Email Tidak Boleh Kosong");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    uPass.setError("Password Tidak Boleh Kosong");
                    return;
                }

                if (password.length() < 8) {
                    uPass.setError("Password Harus >= 8 Characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            fStore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String type = document.getString("type");
                                            if (type.equals("barber")) {
                                                Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivityBarber.class));
                                                finish();
                                            } else if (type.equals("user")) {
                                                Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivityUser.class));
                                                finish();
                                            }
                                        }
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

        });


        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
    }
}