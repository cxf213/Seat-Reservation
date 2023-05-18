package com.cxfwork.libraryappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.regex.Pattern;
import okhttp3.*;
import android.util.Log;

import com.cxfwork.libraryappointment.client.LoginService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    TextInputEditText stuID, password;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = findViewById(R.id.loginbtn);

        OkHttpClient client = new OkHttpClient();
        Request checkloginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/check")
                .build();
        client.newCall(checkloginRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        loginbtn.setEnabled(false);
                    }
                });
                e.printStackTrace();
            }
        });

        TextInputLayout stuIDLayout = findViewById(R.id.StuID);
        TextInputLayout PasswordLayout = findViewById(R.id.Password);
        stuID = (TextInputEditText) stuIDLayout.getEditText();
        password = (TextInputEditText) PasswordLayout.getEditText();
        loginbtn.setOnClickListener(v -> LoginProcess());
        if(LoginService.checkLoginStatus()){
            Nav2MainActivity();
        }
    }

    private void LoginProcess() {
        Nav2MainActivity();
        OkHttpClient client = new OkHttpClient();
        String jsonBody = "{\"username\":\""+stuID.getText().toString()+"\",\"password\":\""+this.password.getText().toString()+"\"}";
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/login")
                .post(requestBody)
                .build();
        client.newCall(loginRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), responseData, Toast.LENGTH_SHORT).show();
                            Nav2MainActivity();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.code()==401){
                                Snackbar.make(findViewById(android.R.id.content), R.string.login_failed,
                                        Snackbar.LENGTH_SHORT).show();
                            }else if(response.code()==402){
                                RegisterProcess();
                            } else if (response.code()==400) {
                                Snackbar.make(findViewById(android.R.id.content), "Enter your username and password",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        loginbtn.setEnabled(false);
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void RegisterProcess() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_register_bottom, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button bottomSheetButton = bottomSheetView.findViewById(R.id.registerbtn);
        CheckBox checkbox = bottomSheetView.findViewById(R.id.acceptEULA);
        TextInputLayout stuNameLayout = bottomSheetView.findViewById(R.id.stuName);
        TextInputLayout stuClassLayout = bottomSheetView.findViewById(R.id.stuClass);
        TextInputLayout stuPhoneNumLayout = bottomSheetView.findViewById(R.id.stuPhoneNum);
        TextInputEditText stuName = (TextInputEditText) stuNameLayout.getEditText();
        TextInputEditText stuClass = (TextInputEditText) stuClassLayout.getEditText();
        TextInputEditText stuPhoneNum = (TextInputEditText) stuPhoneNumLayout.getEditText();
        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox.isChecked()) {
                    Toast.makeText(LoginActivity.this, R.string.acceptEULA, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!stuPhoneNum.getText().toString().matches("^[+]?[0-9]{10,13}$")) {
                    stuPhoneNumLayout.setError("Error");
                    Toast.makeText(LoginActivity.this, R.string.error_phone + stuPhoneNum.getText().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient();
                String jsonBody = "{\"username\":\""+stuID.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\",\"name\":\""+stuName.getText().toString()+"\",\"class\":\""+stuClass.getText().toString()+"\",\"phone\":\""+stuPhoneNum.getText().toString()+"\"}";
                RequestBody requestBody = RequestBody.create(jsonBody, JSON);
                Request loginRequest = new Request.Builder()
                        .url("http://8.130.94.254:8888/register")
                        .post(requestBody)
                        .build();
                client.newCall(loginRequest).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), responseData, Toast.LENGTH_SHORT).show();
                                    Nav2MainActivity();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code()==401){
                                        Snackbar.make(findViewById(android.R.id.content), R.string.login_failed,
                                                Snackbar.LENGTH_SHORT).show();
                                    }else if(response.code()==402){
                                        RegisterProcess();
                                    } else if (response.code()==400) {
                                        Snackbar.make(findViewById(android.R.id.content), "Enter your username and password",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                });


                LoginService.register(stuID.getText().toString(), password.getText().toString(), stuName.getText().toString(), stuClass.getText().toString(), stuPhoneNum.getText().toString());
                bottomSheetDialog.dismiss(); // 关闭 Bottom Sheet
                Nav2MainActivity();
            }
        });
        stuPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                stuPhoneNumLayout.setError(null);
            }
        });

        bottomSheetDialog.show();
    }

    private void Nav2MainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // 可选：关闭 LoginActivity
    }
}