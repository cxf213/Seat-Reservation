package com.cxfwork.libraryappointment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button button;
    private Button button1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        NavController navController = NavHostFragment.findNavController(this);
        button = binding.button1;
        button.setOnClickListener(v -> {
            button.setText(homeViewModel.getText().getValue());
            navController.navigate(R.id.navigation_test);
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        button1 = binding.buttontest1;
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "1按钮被点击", Toast.LENGTH_SHORT).show();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // 找到按钮
                Button bottomSheetButton = bottomSheetView.findViewById(R.id.bottom_sheet_button);
                // 设置按钮的点击事件监听器
                bottomSheetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理按钮的点击事件
                        Toast.makeText(requireContext(), "按钮被点击", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss(); // 关闭 Bottom Sheet
                    }
                });

                bottomSheetDialog.show();
            }

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}