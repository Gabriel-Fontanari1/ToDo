package com.example.todo;

import android.content.Context;
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
        holder.checkBox.setChecked(task.getDone());

        // Riscar o textViewTask quando a caixa for marcada
        if (task.getDone()) {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            notifyItemChanged(position);
        });

        holder.imageButtonRemoveTask.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition(); // Obtém a posição correta
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Tasks taskToRemove = taskList.get(adapterPosition);
                taskList.remove(adapterPosition);
                // Encontra e remove da lista original
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

        // Quando a checkbox for desmarcada
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
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