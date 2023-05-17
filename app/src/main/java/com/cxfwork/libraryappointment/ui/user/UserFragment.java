package com.cxfwork.libraryappointment.ui.user;

import com.cxfwork.libraryappointment.client.LoginService;

import android.content.Intent;
import android.content.res.Configuration;
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
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.LoginActivity;
import com.cxfwork.libraryappointment.MainActivity;
import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.LoginService;
import com.cxfwork.libraryappointment.databinding.FragmentUserBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private boolean isDarkTheme = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavController navController = NavHostFragment.findNavController(this);
        Button navUserInfoButton = binding.userinfobtn;
        navUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_user_info_modify, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // 找到按钮
                Button bottomSheetButton = bottomSheetView.findViewById(R.id.registerbtn);
                // 设置按钮的点击事件监听器
                bottomSheetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理按钮的点击事件
                        Toast.makeText(requireContext(), "按钮被点击", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }

        });
        Button navInfoButton = binding.infobtn;
        navInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.moreInfomationFragment);
        });
        Button navHistoryButton = binding.historybtn;
        navHistoryButton.setOnClickListener(v -> {
            navController.navigate(R.id.reserveHistoryFragment);
        });
        Button themesettingbtn = binding.themesettingbtn;
        themesettingbtn.setOnClickListener(v -> {
            Intent refresh = new Intent(getActivity(), getActivity().getClass());
            startActivity(refresh);
            getActivity().finish();
        });

        Button langettingbtn = binding.langettingbtn;
        langettingbtn.setOnClickListener(v -> {
            Locale currentLocale = getResources().getConfiguration().locale;
            Locale newLocale;
            if (currentLocale.getLanguage().equals("en")) {
                newLocale = new Locale("zh");
            } else {
                Locale.setDefault(Locale.ENGLISH);
                newLocale = new Locale("en");

            }
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();
            config.locale = newLocale;
            getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
            Intent refresh = new Intent(getActivity(), getActivity().getClass());
            startActivity(refresh);
            getActivity().finish();
        });

        Button logout = binding.logoutbtn;
        logout.setOnClickListener(v -> {
            LoginService.logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}