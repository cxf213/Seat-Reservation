package com.cxfwork.libraryappointment;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.cxfwork.libraryappointment.client.ReserveService;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.cxfwork.libraryappointment.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Handler handler;
    private Runnable runnable;
    private CommonViewModel commonViewModel;
    public ReserveService reserveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        reserveService = new ReserveService();
        startDataUpdateTimer();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void updatedata(){
        commonViewModel.setUserReservation1(reserveService.getReserveInfo1());
        commonViewModel.setUserReservation2(reserveService.getReserveInfo2());
    }

    int count = 1;
    private void startDataUpdateTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                commonViewModel.setUserReservation1(reserveService.getReserveInfo1());
                commonViewModel.setUserReservation2(reserveService.getReserveInfo2());
                long delayMillis = 10000; // 5秒
                count++;
                handler.postDelayed(this, delayMillis);
            }
        };

        // 启动首次更新任务
        handler.post(runnable);
    }

}