package com.cxfwork.libraryappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(v -> finishlogin());
    }

    private void finishlogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        //finish(); // 可选：关闭 LoginActivity
    }
}