package com.cxfwork.libraryappointment.ui.user;

import static com.cxfwork.libraryappointment.LoginActivity.JSON;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.LoginActivity;
import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.databinding.FragmentUserBinding;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.cxfwork.libraryappointment.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private boolean isDarkTheme = false;
    SharedPreferences sharedPreferences;
    private CommonViewModel commonViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavController navController = NavHostFragment.findNavController(this);
        sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);

        setUserInfo();

        Button navUserInfoButton = binding.userinfobtn;
        navUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_user_info_modify, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // 找到按钮
                Button bottomSheetButton = bottomSheetView.findViewById(R.id.registerbtn);
                // 设置按钮的点击事件监听器
                bottomSheetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理按钮的点击事件
                        Toast.makeText(requireContext(), "等待审核", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }

        });
        Button navInfoButton = binding.infobtn;
        navInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.moreInfomationFragment);
        });
        Button navHistoryButton = binding.historybtn;
        navHistoryButton.setOnClickListener(v -> {
            navController.navigate(R.id.reserveHistoryFragment);
        });
        Button themesettingbtn = binding.themesettingbtn;
        themesettingbtn.setOnClickListener(v -> {
            Intent refresh = new Intent(getActivity(), getActivity().getClass());
            startActivity(refresh);
            getActivity().finish();
        });


        Button langettingbtn = binding.langettingbtn;
        langettingbtn.setOnClickListener(v -> {
            String language = sharedPreferences.getString("language", "en");
            if(language.equals("en")){
                sharedPreferences.edit().putString("language", "zh").apply();}else{
                sharedPreferences.edit().putString("language", "en").apply();
            }
            setLanguage(sharedPreferences.getString("language", "en"));
            Intent refresh = new Intent(getActivity(), getActivity().getClass());
            startActivity(refresh);
            getActivity().finish();
        });


        Button logout = binding.logoutbtn;
        logout.setOnClickListener(v -> {
            String jwtToken = sharedPreferences.getString("jwt_token", "");
            OkHttpClient client = new OkHttpClient();
            String jsonBody = "{}";
            Log.d("jsonBody", jsonBody);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request loginRequest = new Request.Builder()
                    .url("http://8.130.94.254:8888/logout")
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + jwtToken)
                    .build();
            client.newCall(loginRequest).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("jwt_token");
                    editor.apply();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            });

        });

        return root;
    }

    public void setUserInfo(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        OkHttpClient client = new OkHttpClient();
        String jsonBody = "{}";
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Map<String, String>>() {}.getType();
                            Map<String,String> result = gson.fromJson(responseData, type);
                            binding.stuName.setText(result.get("username").split("#")[0]);
                            binding.stuID.setText(result.get("username").split("#")[1]);;
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;

        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}