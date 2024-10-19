package com.example.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewTask;
    EditText editTextSearchTask;
    EditText textInputEditTextAddTask;
    Button buttonAddTask;
    ArrayList<Tasks> originalList = new ArrayList<>();
    ArrayList<Tasks> taskList = new ArrayList<>();
    TaskAdapter taskAdapter;
    RadioButton radioButtonInProgress;
    RadioButton radioButtonDone;
    RadioButton radioButtonAll;
    RadioGroup radioGroupFilter;

    private AppDatabase db;
    public static TaskDao taskDao;

    private int selectedFilter = R.id.radioButtonAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "todo-database").build();

        taskDao = db.taskDao();

        setContentView(R.layout.activity_main);

        recyclerViewTask = findViewById(R.id.recyclerViewTask);
        editTextSearchTask = findViewById(R.id.editTextSearchTask);
        textInputEditTextAddTask = findViewById(R.id.textInputEditTextAddTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        radioButtonInProgress = findViewById(R.id.radioButtonInProgress);
        radioButtonDone = findViewById(R.id.radioButtonDone);
        radioButtonAll = findViewById(R.id.radioButtonAll);
        radioGroupFilter = findViewById(R.id.radioGroupFilter);

        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTask.setAdapter(taskAdapter);
        recyclerViewTask.setHasFixedSize(true);

        loadTasksFromDatabase();
        addTask();
        setupSearchFilter();

        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            selectedFilter = checkedId;
            filterTasks(editTextSearchTask.getText().toString());
        });
    }

    private void loadTasksFromDatabase() {
        taskDao.getAllTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                originalList.clear();
                originalList.addAll(tasks);
                filterTasks(editTextSearchTask.getText().toString());
            }
        });
    }

    public void addTask() {
        buttonAddTask.setOnClickListener(v -> {
            String taskText = textInputEditTextAddTask.getText().toString();
            if (!taskText.isEmpty()) {
                Tasks newTask = new Tasks(taskText, false);

                new Thread(() -> {
                    taskDao.insertTask(newTask);
                    runOnUiThread(() -> {
                        textInputEditTextAddTask.setText("");
                    });
                }).start();
            }
        });
    }

    private void setupSearchFilter() {
        editTextSearchTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterTasks(String text) {
        ArrayList<Tasks> filteredList = new ArrayList<>();

        for (Tasks task : originalList) {
            if (selectedFilter == R.id.radioButtonAll ||
                    (selectedFilter == R.id.radioButtonDone && task.getDone()) ||
                    (selectedFilter == R.id.radioButtonInProgress && !task.getDone())) {

                if (task.getTask().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(task);
                }
            }
        }

        taskAdapter.filterList(filteredList);
    }
}
