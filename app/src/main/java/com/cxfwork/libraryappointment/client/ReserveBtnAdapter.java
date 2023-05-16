package com.cxfwork.libraryappointment.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cxfwork.libraryappointment.R;

import java.util.List;

public class ReserveBtnAdapter extends RecyclerView.Adapter<ReserveBtnAdapter.ViewHolder> {
    private List<String> RoomsList;
    private ReserveBtnAdapter.OnItemClickListener onItemClickListener;

    public ReserveBtnAdapter(List<String> RoomsList, ReserveBtnAdapter.OnItemClickListener onItemClickListener) {
        this.RoomsList = RoomsList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ReserveBtnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reserve_button, parent, false);
        return new ReserveBtnAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveBtnAdapter.ViewHolder holder, int position) {
        String item = RoomsList.get(position);
        holder.classroomBtn.setText(item);
    }

    @Override
    public int getItemCount() {
        return RoomsList.size();
    }

    public void updateData(List<String> newData) {
        RoomsList = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button classroomBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classroomBtn = itemView.findViewById(R.id.adapter_reserve_btn);
            classroomBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
