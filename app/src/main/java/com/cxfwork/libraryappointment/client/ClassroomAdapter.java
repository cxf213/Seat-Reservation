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

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder> {
    private List<Integer> buttonNumbers;

    public ClassroomAdapter(List<Integer> buttonNumbers) {
        this.buttonNumbers = buttonNumbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reserve_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int number = buttonNumbers.get(position);
        if (position % 2 == 0) {
            // 设置偶数位置的按钮为红色背景
            holder.button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_primary));
        } else {
            // 设置奇数位置的按钮为蓝色背景
            holder.button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_secondary));
        }
        holder.button.setText(String.valueOf("教室编号"+number));
    }

    @Override
    public int getItemCount() {
        return buttonNumbers.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
