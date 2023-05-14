package com.cxfwork.libraryappointment.ui.quickreservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.client.ReserveBtnAdapter;
import com.cxfwork.libraryappointment.databinding.FragmentQuickReservationBinding;

import java.util.ArrayList;
import java.util.List;

public class QuickReservationFragment extends Fragment {

    private FragmentQuickReservationBinding binding;
    private ReserveBtnAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuickReservationViewModel quickReservationViewModel =
                new ViewModelProvider(this).get(QuickReservationViewModel.class);

        binding = FragmentQuickReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        List<Integer> buttonNumbers = generateButtonNumbers(); // 生成按钮编号的数据源

        adapter = new ReserveBtnAdapter(buttonNumbers);
        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<Integer> generateButtonNumbers() {
        List<Integer> buttonNumbers = new ArrayList<>();
        // 在这里生成按钮的编号，并添加到 buttonNumbers 列表中
        for(int i = 0;i<200;i++){
            buttonNumbers.add(i);
        }

        return buttonNumbers;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}