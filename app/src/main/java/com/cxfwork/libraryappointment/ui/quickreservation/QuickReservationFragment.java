package com.cxfwork.libraryappointment.ui.quickreservation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.ClassroomAdapter;
import com.cxfwork.libraryappointment.client.ReserveBtnAdapter;
import com.cxfwork.libraryappointment.databinding.FragmentQuickReservationBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class QuickReservationFragment extends Fragment {

    private FragmentQuickReservationBinding binding;
    private ReserveBtnAdapter reserveBtnAdapter;
    private ClassroomAdapter classroomAdapter;
    private RecyclerView recyclerView,recyclerView2;
    private Button filterbtn;
    private int Date = -1,Timebegin = 2,Timeend = 7,Location = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuickReservationViewModel quickReservationViewModel =
                new ViewModelProvider(this).get(QuickReservationViewModel.class);

        binding = FragmentQuickReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        List<Integer> buttonNumbers = generateButtonNumbers(); // 生成按钮编号的数据源
        reserveBtnAdapter = new ReserveBtnAdapter(buttonNumbers);
        recyclerView.setAdapter(reserveBtnAdapter);
        reserveBtnAdapter.setOnButtonClickListener(new ReserveBtnAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position, View view) {
                // 处理按钮点击事件
                Toast.makeText(view.getContext(), "Button clicked at position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        filterbtn = binding.filterbtn;
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View fragment_filter_selection = getLayoutInflater().inflate(R.layout.fragment_filter_selection, null);
                bottomSheetDialog.setContentView(fragment_filter_selection);

                Button filterCancelBtn = fragment_filter_selection.findViewById(R.id.filterCancelBtn);
                filterCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                // 找到按钮
                Button filterConfirmBtn = fragment_filter_selection.findViewById(R.id.filterreflashBtn);
                // 设置按钮的点击事件监听器
                filterConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Date>=0 && Timebegin>=0 && Timeend>=0 && Location>=0){
                            String sfilter = "Date:"+Date+"Timebegin:"+Timebegin+"Timeend:"+Timeend+"Location:"+Location;
                            //TODO
                            Toast.makeText(requireContext(), sfilter, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(requireContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /***********************************************/
                RangeSlider rangeSlider = fragment_filter_selection.findViewById(R.id.timepicker);
                rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
                    @Override
                    @SuppressLint("RestrictedApi")
                    public void onStartTrackingTouch(@NonNull RangeSlider slider) {}
                    @Override
                    @SuppressLint("RestrictedApi")
                    public void onStopTrackingTouch(@NonNull RangeSlider slider) {

                        List<Float> values = rangeSlider.getValues();
                        float minValue = values.get(0);
                        float maxValue = values.get(1);
                        Timebegin = (int)minValue;
                        Timeend = (int)maxValue;
                        Toast.makeText(requireContext(), "Min value: " + minValue+"Max value: " + maxValue, Toast.LENGTH_SHORT).show();
                    }

                });

                MaterialButtonToggleGroup dateSelector = (MaterialButtonToggleGroup)fragment_filter_selection.findViewById(R.id.dateSelector);
                dateSelector.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                        if(isChecked){
                            if(checkedId == R.id.todaybtn) Date = 1;
                            if(checkedId == R.id.nextdaybtn) Date = 2;
                        }
                    }
                });
                MaterialButtonToggleGroup locationSelector = (MaterialButtonToggleGroup)fragment_filter_selection.findViewById(R.id.locationSelector);
                locationSelector.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                        if(isChecked){
                            if(checkedId == R.id.locationbutton1) Location = 1;
                            if(checkedId == R.id.locationbutton2) Location = 2;
                            if(checkedId == R.id.locationbutton3) Location = 3;
                            if(checkedId == R.id.locationbutton4) Location = 4;
                            if(checkedId == R.id.locationbutton5) Location = 4;
                        }
                    }
                });
                /***********************************************/
                recyclerView2 = fragment_filter_selection.findViewById(R.id.recyclerview2);
                recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1));
                List<Integer> buttonNumbers = generateButtonNumbers(); // 生成按钮编号的数据源
                classroomAdapter = new ClassroomAdapter(buttonNumbers);
                recyclerView2.setAdapter(reserveBtnAdapter);

                bottomSheetDialog.show();
            }

        });

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