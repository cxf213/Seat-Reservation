package com.cxfwork.libraryappointment;

import android.os.Bundle;
import android.os.Handler;

import com.cxfwork.libraryappointment.client.ReserveService;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.cxfwork.libraryappointment.databinding.ActivityMainBinding;

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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



    private void startDataUpdateTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                long delayMillis = 10000; // 5秒
                handler.postDelayed(this, delayMillis);
            }
        };
        handler.post(runnable);
    }

}