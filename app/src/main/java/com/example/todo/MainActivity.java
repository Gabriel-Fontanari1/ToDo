package com.example.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewTask;
    EditText editTextSearchTask;
    EditText textInputEditTextAddTask;
    Button buttonAddTask;
    static List<Tasks> originalList;
    ArrayList<Tasks> taskList;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewTask = findViewById(R.id.recyclerViewTask);
        taskList = new ArrayList<>();
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTask.setAdapter(taskAdapter);
        recyclerViewTask.setHasFixedSize(true);

        editTextSearchTask = findViewById(R.id.editTextSearchTask);
        textInputEditTextAddTask = findViewById(R.id.textInputEditTextAddTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        originalList = new ArrayList<>(taskList);

        addTask();
        setupSearchFilter();
    }

    public void addTask() {
        buttonAddTask.setOnClickListener(View -> {
            String task = textInputEditTextAddTask.getText().toString();
            if (!task.isEmpty()) {
                Tasks newTask = new Tasks(task, false);
                taskList.add(newTask);
                originalList.add(newTask);
                recyclerViewTask.getAdapter().notifyItemInserted(taskList.size() - 1);
                textInputEditTextAddTask.setText("");
            }
        });
    }

    private void setupSearchFilter() {
        editTextSearchTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterTasks(String text) {
        ArrayList<Tasks> filteredList = new ArrayList<>();
        for (Tasks task : originalList) {
            if (task.getTask().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(task);
            }
        }
        taskAdapter.filterList(filteredList);
    }
}