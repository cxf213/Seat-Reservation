package com.cxfwork.libraryappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.*;
import android.util.Log;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    TextInputEditText stuID, password;
    SharedPreferences sharedPreferences;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String language = sharedPreferences.getString("language", "en");
        setLanguage(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = findViewById(R.id.loginbtn);
        TextInputLayout stuIDLayout = findViewById(R.id.StuID);
        TextInputLayout PasswordLayout = findViewById(R.id.Password);
        stuID = (TextInputEditText) stuIDLayout.getEditText();
        password = (TextInputEditText) PasswordLayout.getEditText();
        loginbtn.setOnClickListener(v -> LoginProcess());
        autoLogin();
    }

    private void autoLogin(){
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        OkHttpClient client = new OkHttpClient();
        String jsonBody = "{}";
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/userInfo")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        client.newCall(loginRequest).enqueue(new Callback() {
            List<String> classroomList;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), responseData, Toast.LENGTH_SHORT).show();
                            Nav2MainActivity();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "请重新登录", Toast.LENGTH_SHORT).show();
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

    private void LoginProcess() {
        //Nav2MainActivity();
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
                    String responseBody = response.body().string();
                    String jwtToken;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody);
                        jwtToken = jsonObject.getString("access_token"); // 获取access_token
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("jwt_token", jwtToken);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
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
                String jsonBody = "{\"stuID\":\""+stuID.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\",\"name\":\""+stuName.getText().toString()+"\",\"class\":\""+stuClass.getText().toString()+"\",\"phone\":\""+stuPhoneNum.getText().toString()+"\"}";
                Log.d("TAG","jsonBody");
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
                                    //Nav2MainActivity();
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

    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;

        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void Nav2MainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // 可选：关闭 LoginActivity
    }
}