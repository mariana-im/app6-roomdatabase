package com.example.tareasapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TasksScreen(
    viewModel: TaskViewModel = viewModel(
        factory = TaskViewModel.Factory
    )
) {
// Observa la lista de tareas del ViewModel.
// collectAsStateWithLifecycle deja de escuchar
// cuando la pantalla no está visible.
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
// Estado local: texto del campo de nueva tarea.
    var nuevaTareaTexto by remember { mutableStateOf("") }
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
// ----- Titulo -----
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
// ----- Lista de tareas -----
            Box(modifier = Modifier.weight(1f)) {
                if (tasks.isEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.empty_list_message
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = tasks,
                            key = { task -> task.id }
                        ) { task ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        taskToDelete = task
                                        false
                                    } else {
                                        false
                                    }
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                enableDismissFromEndToStart = true,
                                backgroundContent = {},
                            ) {
                                TaskItem(
                                    task = task,
                                    onToggleCompleted = {
                                        viewModel.toggleCompleted(task)
                                    },
                                    onDelete = {} // ya no se usa
                                )
                            }
                        }
                    }
                }
            }
// ----- Campo para agregar nueva tarea -----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nuevaTareaTexto,
                    onValueChange = { nuevaTareaTexto = it },
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.new_task_placeholder
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.addTask(nuevaTareaTexto)
                        nuevaTareaTexto = ""
                    }
                ) {
                    Text(
                        text = stringResource(R.string.add_button)
                    )
                }
            }
        }
    }

    if (taskToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = {
                Text("Confirmar eliminación")
            },
            text = {
                Text("¿Seguro que quieres eliminar esta tarea?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTask(taskToDelete!!)
                        taskToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
