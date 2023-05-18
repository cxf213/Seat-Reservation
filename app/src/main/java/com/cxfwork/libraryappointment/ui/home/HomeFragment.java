package com.cxfwork.libraryappointment.ui.home;

import static com.cxfwork.libraryappointment.LoginActivity.JSON;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.MainActivity;
import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.databinding.FragmentHomeBinding;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MainActivity mainActivity;
    private CommonViewModel commonViewModel;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();
        NavController navController = NavHostFragment.findNavController(this);
        sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        Button navInfoButton = binding.lookForRecordBtn;
        navInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.reserveHistoryFragment);
        });

        // 绑定卡片和预约数据
        getUserName();
        postUserReservationAction(1, 0);
        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);
        commonViewModel.getUserReservation().observe(this, this::UpdateCards);

        Button signin1btn = binding.Signin1btn;
        signin1btn.setOnClickListener(v -> {
            postUserReservationAction(1, 1);
        });
        Button signin2btn = binding.Signin2btn;
        signin2btn.setOnClickListener(v -> {
            postUserReservationAction(2, 1);
        });
        Button cancel1btn = binding.Cancel1btn;
        cancel1btn.setOnClickListener(v -> {
            postUserReservationAction(1, 2);
        });
        Button cancel2btn = binding.Cancel2btn;
        cancel2btn.setOnClickListener(v -> {
            postUserReservationAction(2, 2);
        });
        return root;
    }

    private void getUserName(){
        String username_id = sharedPreferences.getString("username_id", "NULL");
        if(!username_id.equals("NULL")){
            binding.StuName.setText(username_id.split("#")[0]);
        }
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
                            sharedPreferences.edit().putString("username_id", result.get("username")).apply();
                            binding.StuName.setText(result.get("username").split("#")[0]);
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

    private void postUserReservationAction(int id, int action){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        String jsonBody = "{\"id\":\""+id+"\",\"action\":\""+action+"\"}";
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/userReservation")
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
                            Log.d("responseData", result.toString());
                            commonViewModel.setUserReservation(result);
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
    private void UpdateCard1Info(Map<String, String> UserReservation){
        binding.card1SeatPos.setText(UserReservation.get("Seat1"));
        binding.card1SeatPos2.setText(UserReservation.get("location1"));
        binding.card1SeatTime.setText(UserReservation.get("time1"));
    }
    private void UpdateCard2Info(Map<String, String> UserReservation){
        binding.card2SeatPos.setText(UserReservation.get("Seat2"));
        binding.card2SeatPos2.setText(UserReservation.get("location2"));
        binding.card2SeatTime.setText(UserReservation.get("time2"));
    }
    public void UpdateCards(Map<String, String> UserReservation) {
        if(UserReservation.get("haveReservation1") == null){
            return;
        }
        if(UserReservation.get("haveReservation1").equals("1")){
            UpdateCard1Info(UserReservation);
            binding.card1.setVisibility(View.VISIBLE);
        }else{
            binding.card1.setVisibility(View.GONE);
        }

        if(UserReservation.get("haveSignin1").equals("1")) {
            binding.Signin1btn.setText("Signed in");
            binding.Signin1btn.setEnabled(false);
        }else{
            binding.Signin1btn.setText("Sign in");
            binding.Signin1btn.setEnabled(true);
        }

        if(UserReservation.get("haveReservation2").equals("1")){
            UpdateCard2Info(UserReservation);
            binding.card2.setVisibility(View.VISIBLE);
        }else{
            binding.card2.setVisibility(View.GONE);
        }

        if(UserReservation.get("haveSignin2").equals("1")) {
            binding.Signin2btn.setText("Signed in");
            binding.Signin2btn.setEnabled(false);
        }else{
            binding.Signin2btn.setText("Sign in");
            binding.Signin2btn.setEnabled(true);
        }
        if(binding.card1.getVisibility() == View.GONE && binding.card2.getVisibility() == View.GONE)
            binding.cardno.setVisibility(View.VISIBLE);
        else
            binding.cardno.setVisibility(View.GONE);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}