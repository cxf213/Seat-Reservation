package com.cxfwork.libraryappointment.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrentReservationAdapter extends RecyclerView.Adapter<CurrentReservationAdapter.ViewHolder> {
    private List<Map<String, String>> dataList;
    private OnButtonClickListener buttonClickListener;
    public CurrentReservationAdapter(List<Map<String, String>> dataList,OnButtonClickListener buttonClickListener) {
        this.dataList = dataList;
        this.buttonClickListener = buttonClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_reservation, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getBindingAdapterPosition();
        Map<String, String> data = dataList.get(currentPosition);
        String seatPos = data.get("seatPos");
        String seatPos2 = data.get("seatPos2");
        String seatTime = data.get("seatTime");

        holder.card1SeatPos.setText(seatPos);
        holder.card1SeatPos2.setText(seatPos2);
        holder.card1SeatTime.setText(seatTime);
        holder.signinbtn.setEnabled(!data.get("statue").equals("Signed"));
        holder.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonClickListener != null) {
                    buttonClickListener.onSigninButtonClick(currentPosition);
                }
            }
        });

        holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonClickListener != null) {
                    buttonClickListener.onCancelButtonClick(currentPosition);
                }
            }
        });
    }
    public interface OnButtonClickListener {
        void onSigninButtonClick(int position);
        void onCancelButtonClick(int position);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void updateData(List<Map<String, String>> newData) {
        this.dataList = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card1SeatPos;
        TextView card1SeatPos2;
        TextView card1SeatTime;
        Button signinbtn;
        Button cancelbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card1SeatPos = itemView.findViewById(R.id.card1SeatPos);
            card1SeatPos2 = itemView.findViewById(R.id.card1SeatPos2);
            card1SeatTime = itemView.findViewById(R.id.card1SeatTime);
            signinbtn = itemView.findViewById(R.id.signinbtn);
            cancelbtn = itemView.findViewById(R.id.cancelbtn);
        }
    }
}
