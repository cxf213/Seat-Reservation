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
    private List<Integer> buttonNumbers;
    private static OnButtonClickListener onButtonClickListener;

    public ReserveBtnAdapter(List<Integer> buttonNumbers) {
        this.buttonNumbers = buttonNumbers;
    }
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
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
        holder.button.setText(String.valueOf(number));
    }



    @Override
    public int getItemCount() {
        return buttonNumbers.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.adapter_reserve_btn);

            // 设置按钮的点击事件监听器
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 获取按钮在列表中的位置
                    int position = getAdapterPosition();

                    // 检查监听器是否存在
                    if (onButtonClickListener != null) {
                        // 调用监听器的回调方法，传递位置和按钮视图
                        onButtonClickListener.onButtonClick(position, v);
                    }
                }
            });
        }
    }
    public interface OnButtonClickListener {
        void onButtonClick(int position, View view);
    }
}
