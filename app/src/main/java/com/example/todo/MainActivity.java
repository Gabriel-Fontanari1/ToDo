package com.example.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
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
    static List<Tasks> originalList;
    ArrayList<Tasks> taskList;
    TaskAdapter taskAdapter;
    RadioButton radioButtonInProgress;
    RadioButton radioButtonDone;
    RadioButton radioButtonAll;
    RadioGroup radioGroupFilter;

    private AppDatabase db;
    public static TaskDao taskDao;

    // Variável para armazenar o filtro selecionado
    private int selectedFilter = R.id.radioButtonAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instanciar o banco de dados Room
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "todo-database").allowMainThreadQueries().build();

        taskDao = db.taskDao();

        setContentView(R.layout.activity_main);

        // Inicialização das views
        recyclerViewTask = findViewById(R.id.recyclerViewTask);
        editTextSearchTask = findViewById(R.id.editTextSearchTask);
        textInputEditTextAddTask = findViewById(R.id.textInputEditTextAddTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        radioButtonInProgress = findViewById(R.id.radioButtonInProgress);
        radioButtonDone = findViewById(R.id.radioButtonDone);
        radioButtonAll = findViewById(R.id.radioButtonAll);
        radioGroupFilter = findViewById(R.id.radioGroupFilter);

        // Configuração do RecyclerView
        taskList = new ArrayList<>();
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTask.setAdapter(taskAdapter);
        recyclerViewTask.setHasFixedSize(true);

        originalList = new ArrayList<>(taskList);

        // Carregar as tarefas persistidas no banco de dados
        loadTasksFromDatabase();

        // Configuração dos listeners
        addTask();
        setupSearchFilter();

        // Filtros dos RadioButtons
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            selectedFilter = checkedId;  // Atualiza o filtro selecionado
            filterTasks(editTextSearchTask.getText().toString());  // Refiltra com base no texto atual da searchbar
        });
    }

    // Carregar tarefas do banco de dados Room
    private void loadTasksFromDatabase() {
        List<Tasks> tasksFromDb = taskDao.getAllTasks();
        taskList.addAll(tasksFromDb);
        originalList.addAll(tasksFromDb);
        taskAdapter.notifyDataSetChanged();
    }

    // Método para adicionar nova tarefa
    public void addTask() {
        buttonAddTask.setOnClickListener(v -> {
            String taskText = textInputEditTextAddTask.getText().toString();
            if (!taskText.isEmpty()) {
                Tasks newTask = new Tasks(taskText, false);

                // Adicionar nova tarefa à lista
                taskList.add(newTask);
                originalList.add(newTask);

                // Persistir nova tarefa no banco de dados
                taskDao.insertTask(newTask);

                recyclerViewTask.getAdapter().notifyItemInserted(taskList.size() - 1);
                textInputEditTextAddTask.setText(""); // Limpar o campo de input
            }
        });
    }

    // Configuração da busca de tarefas
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

    // Filtro de tarefas baseado no texto digitado e no filtro ativo
    private void filterTasks(String text) {
        ArrayList<Tasks> filteredList = new ArrayList<>();

        // Primeiro, aplicar o filtro baseado no status (All, Done, In Progress)
        for (Tasks task : originalList) {
            if (selectedFilter == R.id.radioButtonAll ||
                    (selectedFilter == R.id.radioButtonDone && task.getDone()) ||
                    (selectedFilter == R.id.radioButtonInProgress && !task.getDone())) {

                // Em seguida, aplicar o filtro de texto
                if (task.getTask().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(task);
                }
            }
        }

        // Atualizar o adapter com a lista filtrada
        taskAdapter.filterList(filteredList);
    }
}
