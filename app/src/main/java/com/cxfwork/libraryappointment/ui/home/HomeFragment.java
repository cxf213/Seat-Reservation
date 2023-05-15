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
                if(binding.card1.getVisibility() == View.GONE && binding.card2.getVisibility() == View.GONE)
                    binding.cardno.setVisibility(View.VISIBLE);
                else
                    binding.cardno.setVisibility(View.GONE);
                if(UserReservation1.get("haveReservation").equals("1")){
                        binding.card1.setVisibility(View.VISIBLE);
                }else{
                        binding.card1.setVisibility(View.GONE);
                }
                if(UserReservation1.get("haveSignin").equals("1")) {
                    binding.Signin1btn.setText("Signed in");
                    binding.Signin1btn.setEnabled(false);
                }else{
                    binding.Signin1btn.setText("Sign in");
                    binding.Signin1btn.setEnabled(true);
                }
            }
        });
        commonViewModel.getUserReservation2().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> UserReservation2) {
                if(binding.card1.getVisibility() == View.GONE && binding.card2.getVisibility() == View.GONE)
                    binding.cardno.setVisibility(View.VISIBLE);
                else
                    binding.cardno.setVisibility(View.GONE);
                if(UserReservation2.get("haveReservation").equals("1")){
                        binding.card2.setVisibility(View.VISIBLE);
                }else{
                        binding.card2.setVisibility(View.GONE);
                }
                if(UserReservation2.get("haveSignin").equals("1")) {
                    binding.Signin2btn.setText("Signed in");
                    binding.Signin2btn.setEnabled(false);
                }else{
                    binding.Signin2btn.setText("Sign in");
                    binding.Signin2btn.setEnabled(true);
                }
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