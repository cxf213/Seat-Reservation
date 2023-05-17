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

import java.util.regex.Pattern;

import com.cxfwork.libraryappointment.client.LoginService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    TextInputEditText stuID, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = findViewById(R.id.loginbtn);

        if(!LoginService.checkNetwork()){
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            loginbtn.setEnabled(false);
            return;
        }

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
        String username = stuID.getText().toString();
        String password = this.password.getText().toString();
        String result = LoginService.login(username, password);
        if (result.equals("Success")) {
            Nav2MainActivity();
        } else if (result.equals("Register")) {
            RegisterProcess();
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.login_failed,
                    Snackbar.LENGTH_SHORT).show();
        }
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
                // 在文本变化之后的回调方法
                // 清除错误提示
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