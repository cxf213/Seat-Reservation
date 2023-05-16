package com.cxfwork.libraryappointment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.MainActivity;
import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.databinding.FragmentHomeBinding;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CommonViewModel commonViewModel;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();

        NavController navController = NavHostFragment.findNavController(this);
        Button navInfoButton = binding.lookForRecordBtn;
        navInfoButton.setOnClickListener(v -> {
            navInfoButton.setText(homeViewModel.getText().getValue());
            navController.navigate(R.id.reserveHistoryFragment);
        });

        // 绑定卡片和预约数据
        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);
        commonViewModel.getUserReservation().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> UserReservation1) {
                UpdateCards(UserReservation1);
            }
        });

        Button signin1btn = binding.Signin1btn;
        signin1btn.setOnClickListener(v -> {
            mainActivity.reserveService.signin1();
            mainActivity.updatedata();
        });
        Button signin2btn = binding.Signin2btn;
        signin2btn.setOnClickListener(v -> {
            mainActivity.reserveService.signin2();
            mainActivity.updatedata();
        });
        Button cancel1btn = binding.Cancel1btn;
            cancel1btn.setOnClickListener(v -> {
            mainActivity.reserveService.cancel1();
            mainActivity.updatedata();
        });
        Button cancel2btn = binding.Cancel2btn;
        cancel2btn.setOnClickListener(v -> {
            mainActivity.reserveService.cancel2();
            mainActivity.updatedata();
        });




        return root;
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