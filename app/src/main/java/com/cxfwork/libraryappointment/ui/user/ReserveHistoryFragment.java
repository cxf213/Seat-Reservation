package com.cxfwork.libraryappointment.ui.user;

import static com.cxfwork.libraryappointment.LoginActivity.JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.ReserveHistoryAdapter;
import com.cxfwork.libraryappointment.ui.quickreservation.QuickReservationFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReserveHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReserveHistoryAdapter reserveHistoryAdapter;
    SharedPreferences sharedPreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reserve_history, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        recyclerView = rootView.findViewById(R.id.ReserveHistoryRecyclerView);

        OkHttpClient client = new OkHttpClient();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        Gson gson = new Gson();
        String jsonBody = "{}";;
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
                            Log.d("responseData", responseData);
                            Type type = new TypeToken<List<Map<String, String>>>() {}.getType();
                            List<Map<String, String>> dataList = gson.fromJson(responseData, type);
                            reserveHistoryAdapter = new ReserveHistoryAdapter(dataList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(reserveHistoryAdapter);
                        }
                    });
                } else {
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
        return rootView;
    }
}