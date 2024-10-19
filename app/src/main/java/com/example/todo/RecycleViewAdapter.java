package com.example.todo;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Tasks> taskList;
    private Context context;

    public TaskAdapter(List<Tasks> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_line_todo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tasks task = taskList.get(position);
        holder.textTask.setText(task.getTask());

        // Desabilitar temporariamente o listener do checkbox ao fazer a ligação de dados
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.getDone());

        if (task.getDone()) {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Reconfigurar o listener do checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);

            int originalListIndex = MainActivity.originalList.indexOf(task);
            if (originalListIndex != -1) {
                MainActivity.originalList.get(originalListIndex).setDone(isChecked);
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                notifyItemChanged(holder.getAdapterPosition());
            });

            if (isChecked) {
                holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        holder.imageButtonRemoveTask.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Tasks taskToRemove = taskList.get(adapterPosition);
                taskList.remove(adapterPosition);
                for (int i = 0; i < MainActivity.originalList.size(); i++) {
                    if (MainActivity.originalList.get(i).getTask().equals(taskToRemove.getTask())) {
                        MainActivity.originalList.remove(i);
                        break;
                    }
                }
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, taskList.size());
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTask;
        CheckBox checkBox;
        ImageButton imageButtonRemoveTask;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.textViewTask);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageButtonRemoveTask = itemView.findViewById(R.id.imageButtonRemoveTask);
        }
    }

    public void filterList(ArrayList<Tasks> filteredList) {
        this.taskList = filteredList;
        notifyDataSetChanged();
    }
}