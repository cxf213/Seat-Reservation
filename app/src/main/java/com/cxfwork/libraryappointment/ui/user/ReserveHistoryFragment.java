package com.cxfwork.libraryappointment.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxfwork.libraryappointment.R;
import com.cxfwork.libraryappointment.client.ReserveHistoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReserveHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReserveHistoryAdapter reserveHistoryAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reserve_history, container, false);

        recyclerView = rootView.findViewById(R.id.ReserveHistoryRecyclerView);

        // 创建适配器实例，传入数据列表
        List<Map<String, String>> dataList = generateDataList(); // 替换成您的数据列表
        reserveHistoryAdapter = new ReserveHistoryAdapter(dataList);

        // 设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 设置适配器
        recyclerView.setAdapter(reserveHistoryAdapter);

        return rootView;
    }
    private List<Map<String, String>> generateDataList() {
        // 替换成您生成数据列表的逻辑
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data = new java.util.HashMap<>();
        data.put("seatPos2", "博一A101");
        data.put("seatPos", "26号座位");
        data.put("seatTime", "2021-06-01 12:00-13:00");
        dataList.add(data);
        // 添加数据到 dataList

        return dataList;
    }
}