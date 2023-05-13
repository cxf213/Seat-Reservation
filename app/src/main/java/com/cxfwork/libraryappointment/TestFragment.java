package com.cxfwork.libraryappointment;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;

import com.cxfwork.libraryappointment.databinding.FragmentTestBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class TestFragment extends Fragment {

    private TestViewModel mViewModel;

    public static TestFragment newInstance() {
        return new TestFragment();
    }
    NavController navController;
    private Button button;
    private FragmentTestBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        navController = NavHostFragment.findNavController(this);
        setHasOptionsMenu(true);

        binding = FragmentTestBinding.inflate(inflater, container, false);
        binding.buttontest.setText("test");
        button = binding.buttontest;
        button.setText("test");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "1按钮被点击", Toast.LENGTH_SHORT).show();
            }

        });

        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        Button myButton = rootView.findViewById(R.id.buttontest);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // 添加返回按钮项到选项菜单中
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("第二个页面");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 执行自定义的返回操作，例如执行导航操作或其他逻辑
            navController.popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TestViewModel.class);
        // TODO: Use the ViewModel
    }

}