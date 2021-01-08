package com.ribal.cutline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ribal.cutline.R;

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void barberReg(View view) {
        Intent i = new Intent(view.getContext(),RegisterForm.class);
        i.putExtra("type","barber");
        startActivity(i);
        finish();
    }

    public void userReg(View view) {
        Intent i = new Intent(view.getContext(),RegisterForm.class);
        i.putExtra("type","user");
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}