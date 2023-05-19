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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.MainActivity;
import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.CurrentReservationAdapter;
import com.cxfwork.libraryappointment.client.ReserveHistoryAdapter;
import com.cxfwork.libraryappointment.databinding.FragmentHomeBinding;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment implements CurrentReservationAdapter.OnButtonClickListener{

    private FragmentHomeBinding binding;
    private MainActivity mainActivity;
    private CommonViewModel commonViewModel;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private CurrentReservationAdapter currentReservationAdapter;
    private boolean initData = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();
        NavController navController = NavHostFragment.findNavController(this);
        sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);



        recyclerView = binding.CurrentReserveDisplay;
        postUserReservationAction("", "",this);


        Button navInfoButton = binding.lookForRecordBtn;
        navInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.reserveHistoryFragment);
        });

        getUserName();
        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);
        return root;
    }

    @Override
    public void onSigninButtonClick(int position) {
        List<Map<String, String>> dataList = currentReservationAdapter.getDataList();
        String currentid = dataList.get(position).get("id");
        Log.d("currentid", currentid);
        postUserReservationAction(currentid, "signin",this);
    }

    @Override
    public void onCancelButtonClick(int position) {

    }


    private void getUserName(){
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

    private void postUserReservationAction(String id, String action, CurrentReservationAdapter.OnButtonClickListener onButtonClickListener){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        String jsonBody = "{\"id\":\""+id+"\",\"action\":\""+action+"\"}";
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/history")
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
                            Type type = new TypeToken<List<Map<String, String>>>() {}.getType();
                            List<Map<String, String>> result = gson.fromJson(responseData, type);
                            Log.d("responseData", result.toString());
                            if(Objects.equals(id, "")){
                                currentReservationAdapter = new CurrentReservationAdapter(result,onButtonClickListener);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(currentReservationAdapter);
                            }else{
                                currentReservationAdapter.updateData(result);
                            }
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}