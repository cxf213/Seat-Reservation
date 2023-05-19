package com.cxfwork.libraryappointment.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.R;

import java.util.List;
import java.util.Map;

public class ReserveHistoryAdapter extends RecyclerView.Adapter<ReserveHistoryAdapter.ViewHolder> {
    private List<Map<String, String>> dataList;

    public ReserveHistoryAdapter(List<Map<String, String>> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reservation_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> data = dataList.get(position);

        String seatPos = data.get("seatID");
        String seatPos2 = data.get("seatTime");
        String seatTime = data.get("seatTime");

        holder.card1SeatPos.setText(seatPos);
        holder.card1SeatPos2.setText(seatPos2);
        holder.card1SeatTime.setText(seatTime);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card1SeatPos;
        TextView card1SeatPos2;
        TextView card1SeatTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card1SeatPos = itemView.findViewById(R.id.card1SeatPos);
            card1SeatPos2 = itemView.findViewById(R.id.card1SeatPos2);
            card1SeatTime = itemView.findViewById(R.id.card1SeatTime);
        }
    }
}
