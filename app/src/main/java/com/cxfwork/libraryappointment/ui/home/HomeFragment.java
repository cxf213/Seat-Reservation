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
    private Button button;
    private CommonViewModel commonViewModel;
    private int reserveCount = 0;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();

        NavController navController = NavHostFragment.findNavController(this);
        button = binding.lookForRecordBtn;
        button.setOnClickListener(v -> {
            button.setText(homeViewModel.getText().getValue());
            navController.navigate(R.id.navigation_test);
        });

        binding.card1.setVisibility(View.GONE);
        binding.card2.setVisibility(View.GONE);

        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);
        commonViewModel.getUserReservation1().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> UserReservation1) {
                if(UserReservation1.get("haveReservation").equals("1")){
                    if(binding.card1.getVisibility() == View.GONE) {
                        reserveCount += 1;
                        binding.card1.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(binding.card1.getVisibility() == View.VISIBLE){
                        reserveCount -= 1;
                        binding.card1.setVisibility(View.GONE);
                    }
                }
                if(reserveCount == 0) binding.cardno.setVisibility(View.VISIBLE);
                else binding.cardno.setVisibility(View.GONE);
            }
        });
        commonViewModel.getUserReservation2().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> UserReservation2) {
                if(UserReservation2.get("haveReservation").equals("1")){
                    if(binding.card2.getVisibility() == View.GONE) {
                        reserveCount += 1;
                        binding.card2.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(binding.card2.getVisibility() == View.VISIBLE){
                        reserveCount -= 1;
                        binding.card2.setVisibility(View.GONE);
                    }
                }
                if(reserveCount == 0) binding.cardno.setVisibility(View.VISIBLE);
                else binding.cardno.setVisibility(View.GONE);
            }
        });

        Button signin1btn = binding.Signin1btn;
        signin1btn.setOnClickListener(v -> {
            mainActivity.reserveService.signin1();
            signin1btn.setText("Signed in");
            signin1btn.setEnabled(false);
            mainActivity.updatedata();
        });
        Button signin2btn = binding.Signin2btn;
        signin2btn.setOnClickListener(v -> {
            mainActivity.reserveService.signin2();
            signin2btn.setText("Signed in");
            signin2btn.setEnabled(false);
            mainActivity.updatedata();
        });
        Button cancel1btn = binding.Cancel1btn;
            cancel1btn.setOnClickListener(v -> {
            mainActivity.reserveService.cancel1();
            cancel1btn.setText("Canceling");
            cancel1btn.setEnabled(false);
            mainActivity.updatedata();
        });
        Button cancel2btn = binding.Cancel2btn;
        cancel2btn.setOnClickListener(v -> {
            mainActivity.reserveService.cancel2();
            cancel2btn.setText("Canceling");
            cancel2btn.setEnabled(false);
            mainActivity.updatedata();
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
//                View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
//                bottomSheetDialog.setContentView(bottomSheetView);
//
//                // 找到按钮
//                Button bottomSheetButton = bottomSheetView.findViewById(R.id.bottom_sheet_button);
//                // 设置按钮的点击事件监听器
//                bottomSheetButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 处理按钮的点击事件
//                        Toast.makeText(requireContext(), "按钮被点击", Toast.LENGTH_SHORT).show();
//                        bottomSheetDialog.dismiss(); // 关闭 Bottom Sheet
//                    }
//                });
//
//                bottomSheetDialog.show();
//            }
//
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}