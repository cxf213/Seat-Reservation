package com.cxfwork.libraryappointment.ui.quickreservation;

import static com.cxfwork.libraryappointment.LoginActivity.JSON;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.ClassroomAdapter;
import com.cxfwork.libraryappointment.client.ReserveBtnAdapter;
import com.cxfwork.libraryappointment.client.ReserveService;
import com.cxfwork.libraryappointment.databinding.FragmentQuickReservationBinding;
import com.cxfwork.libraryappointment.ui.CommonViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;

public class QuickReservationFragment extends Fragment {

    private FragmentQuickReservationBinding binding;
    private ReserveBtnAdapter reserveBtnAdapter;
    private ClassroomAdapter classroomAdapter;
    private CommonViewModel commonViewModel;
    private SharedPreferences sharedPreferences;
    private String displaystr;
    List<String> seatsNamesList;
    List<String> roomsNamesList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuickReservationViewModel quickReservationViewModel =
                new ViewModelProvider(this).get(QuickReservationViewModel.class);

        binding = FragmentQuickReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        commonViewModel = new ViewModelProvider(requireActivity()).get(CommonViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        if (commonViewModel.getNewReservation().getValue().isEmpty())
            commonViewModel.setNewReservation(generateBasicFilter());
        Button filterbtn = binding.filterbtn;
        seatsNamesList = new ArrayList<>();
        updateSeatsAdapter();
        RecyclerView recyclerView = binding.roomrecyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        reserveBtnAdapter = new ReserveBtnAdapter(seatsNamesList, new ReserveBtnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 最终预约
                String[] selectedString = seatsNamesList.get(position).split("#");
                commonViewModel.updateNewReservationValue("Seat", selectedString[0]);
                dialogDisplay(selectedString);
            }
        });
        recyclerView.setAdapter(reserveBtnAdapter);


        //确定筛选器后，筛选器的数据将被存到这里，以便生成请求
        commonViewModel.getNewReservation().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> NewReservation) {
                displaystr = (NewReservation.get("DateID").equals("1") ? "今天" : "明天") + " 第" +
                        (Integer.parseInt(Objects.requireNonNull(NewReservation.get("TimeIDbegin"))) + 1) + "-" +
                        NewReservation.get("TimeIDend") + "节课 博学" +
                        NewReservation.get("Building") + "楼  " +
                        NewReservation.get("Room");
                filterbtn.setText(displaystr);
            }
        });
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSheetProcess(v);
            }
        });

        updateClassroomAdapter();
        return root;
    }

    private void filterSheetProcess(View v) {
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

        Log.d("TAG", "filterSheetProcess: "+roomsNamesList);



        RecyclerView recyclerView2 = fragment_filter_selection.findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        classroomAdapter = new ClassroomAdapter(roomsNamesList, new ClassroomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String[] selectedString = roomsNamesList.get(position).split("#");
                if(!selectedString[1].equals("0")){
                    Toast.makeText(requireContext(), "该教室无法预约:"+selectedString[1], Toast.LENGTH_SHORT).show();
                    return;
                }
                commonViewModel.updateNewReservationValue("Room", selectedString[0]);
                updateSeatsAdapter();
                bottomSheetDialog.dismiss();
            }
        });
        recyclerView2.setAdapter(classroomAdapter);

        Button filterReflashBtn = fragment_filter_selection.findViewById(R.id.filterreflashBtn);
        filterReflashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClassroomAdapter();
            }
        });


        /* ============================================ */
        RangeSlider rangeSlider = fragment_filter_selection.findViewById(R.id.timepicker);
        rangeSlider.setValues(Float.parseFloat(Objects.requireNonNull(commonViewModel.getNewReservation().getValue().get("TimeIDbegin"))),
                Float.parseFloat(Objects.requireNonNull(commonViewModel.getNewReservation().getValue().get("TimeIDend"))));
        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            @SuppressLint("RestrictedApi")
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
            }

            @Override
            @SuppressLint("RestrictedApi")
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List<Float> values = rangeSlider.getValues();
                commonViewModel.updateNewReservationValue("TimeIDbegin", String.valueOf(((int) (float) values.get(0))));
                commonViewModel.updateNewReservationValue("TimeIDend", String.valueOf(((int) (float) values.get(1))));
                updateClassroomAdapter();
            }

        });

        MaterialButtonToggleGroup dateSelector = (MaterialButtonToggleGroup) fragment_filter_selection.findViewById(R.id.dateSelector);
        dateSelector.check(commonViewModel.getNewReservation().getValue().get("DateID").equals("1") ? R.id.todaybtn : R.id.tomorrowbtn);
        dateSelector.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    commonViewModel.updateNewReservationValue("DateID", checkedId == R.id.todaybtn ? "1" : "2");
                    updateClassroomAdapter();
                }
            }
        });
        MaterialButtonToggleGroup locationSelector = (MaterialButtonToggleGroup) fragment_filter_selection.findViewById(R.id.locationSelector);
        List<Integer> locationButtonList = new ArrayList<>();
        locationButtonList.add(R.id.locationbutton1);
        locationButtonList.add(R.id.locationbutton2);
        locationButtonList.add(R.id.locationbutton3);
        locationButtonList.add(R.id.locationbutton4);
        locationButtonList.add(R.id.locationbutton5);
        locationSelector.check(locationButtonList.get(Integer.parseInt(Objects.requireNonNull(commonViewModel.getNewReservation().getValue().get("Building"))) - 1));
        locationSelector.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    commonViewModel.updateNewReservationValue("Building", String.valueOf(locationButtonList.indexOf(checkedId) + 1));
                    updateClassroomAdapter();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void dialogDisplay(String[] selectedString){
        if(selectedString[1].equals("0")){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle(R.string.reservationDialogConfirmTitle)
                    .setMessage(displaystr + " " + selectedString[0] + "号座位")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reserve(dialog,which,1);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle(R.string.reserveError)
                    .setMessage(getString(R.string.reserveFailure,displaystr+selectedString[0] + "号"))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reserve(dialog,which,0);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void reserve(DialogInterface dialog, int which,int statue){
        OkHttpClient client = new OkHttpClient();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        Gson gson = new Gson();
        String jsonBody = gson.toJson(commonViewModel.getNewReservation().getValue());;
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/reserve")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        client.newCall(loginRequest).enqueue(new Callback() {
            List<String> classroomList;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("responseData", responseData);
                            Gson gson = new Gson();
                            Type type = new TypeToken<Map<String, String>>() {}.getType();
                            Map<String,String> result = gson.fromJson(responseData, type);

                            if(statue == 1){
                                if(result.get("status").equals("1")) {
                                    Toast.makeText(requireContext(), R.string.reserveSuccessTitle, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(requireContext(), R.string.reserveError +result.get("message"), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(requireContext(), result.get("name"), Toast.LENGTH_SHORT).show();
                                Uri uri = Uri.parse("tel:" + result.get("phone"));
                                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                                if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(requireContext(), "No Permissions", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.code()==401){

                            }else if(response.code()==402){

                            }else if(response.code()==400) {

                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void updateClassroomAdapter(){
        OkHttpClient client = new OkHttpClient();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        Gson gson = new Gson();
        String jsonBody = gson.toJson(commonViewModel.getNewReservation().getValue());;
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/rooms")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        client.newCall(loginRequest).enqueue(new Callback() {
            List<String> classroomList;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("responseData", responseData);
                            Gson gson = new Gson();

                            Type dataType = new TypeToken<jsondatatype>() {}.getType();
                            jsondatatype myData = gson.fromJson(responseData, dataType);
                            roomsNamesList = new ArrayList<>(myData.data);
                            if(classroomAdapter != null)
                                classroomAdapter.updateData(myData.data); //更新adapter的数据
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.code()==401){

                            }else if(response.code()==402){

                            }else if(response.code()==400) {

                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void updateSeatsAdapter(){
        OkHttpClient client = new OkHttpClient();
        String jwtToken = sharedPreferences.getString("jwt_token", "");
        Gson gson = new Gson();
        String jsonBody = gson.toJson(commonViewModel.getNewReservation().getValue());;
        Log.d("jsonBody", jsonBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request loginRequest = new Request.Builder()
                .url("http://8.130.94.254:8888/seats")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        client.newCall(loginRequest).enqueue(new Callback() {
            List<String> classroomList;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("responseData", responseData);
                            Gson gson = new Gson();

                            Type dataType = new TypeToken<jsondatatype>() {}.getType();
                            jsondatatype myData = gson.fromJson(responseData, dataType);
                            seatsNamesList = new ArrayList<>(myData.data);
                            if(reserveBtnAdapter != null)
                                reserveBtnAdapter.updateData(myData.data); //更新adapter的数据
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.code()==401){

                            }else if(response.code()==402){

                            }else if(response.code()==400) {

                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    public class jsondatatype {public List<String> data;}
    private Map<String, String> generateBasicFilter() {
        Map<String, String> basicFilter = new HashMap<>();
        basicFilter.put("DateID", "2");
        basicFilter.put("TimeIDbegin", "0");
        basicFilter.put("TimeIDend", "2");
        basicFilter.put("Building", "1");
        basicFilter.put("Room", "A101");
        return basicFilter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}