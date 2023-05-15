package com.cxfwork.libraryappointment.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavController navController = NavHostFragment.findNavController(this);
        Button navUserInfoButton = binding.userinfobtn;
        navUserInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.userInfoModifyFragment);
        });
        Button navInfoButton = binding.infobtn;
        navInfoButton.setOnClickListener(v -> {
            navController.navigate(R.id.moreInfomationFragment);
        });
        Button navHistoryButton = binding.historybtn;
        navHistoryButton.setOnClickListener(v -> {
            navController.navigate(R.id.reserveHistoryFragment);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}