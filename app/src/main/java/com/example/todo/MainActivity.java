package com.example.todo;

import android.os.Bundle;
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

//ideia: fazer um app para adicionar tarefas a serem feitas.
//vai ser possível: add novas tarefas, exclui-las, separa-las por filtros, como concluido, não concluido, in progress, etc.
public class MainActivity extends AppCompatActivity {

    //atributos
    RecyclerView recyclerViewTask;
    EditText editTextSearchTask;
    EditText textInputEditTextAddTask;
    Button buttonAddTask;
    List tasks;
    ArrayList<Tasks> taskList;


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

        //recyclerview
        recyclerViewTask = findViewById(R.id.recyclerViewTask);
        taskList = new ArrayList<>();
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTask.setAdapter(new TaskAdapter(taskList, this));
        recyclerViewTask.setHasFixedSize(true);

        //puxar as views
        recyclerViewTask = findViewById(R.id.recyclerViewTask);
        editTextSearchTask = findViewById(R.id.editTextSearchTask);
        textInputEditTextAddTask = findViewById(R.id.textInputEditTextAddTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        addTask();
    }

    //metodos
    //metodo do botão addtaks
    public void addTask() {
        buttonAddTask.setOnClickListener(View -> {
            String task = textInputEditTextAddTask.getText().toString();
            if (!task.isEmpty()) {
                Tasks newTask = new Tasks(task, false);
                taskList.add(newTask);
                recyclerViewTask.getAdapter().notifyItemInserted(taskList.size() - 1);
                textInputEditTextAddTask.setText("");
            }
        });
    }
}