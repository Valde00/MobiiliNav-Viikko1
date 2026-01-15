package com.example.viikko1.domain

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.ui.theme.Viikko1Theme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    showDone: Boolean? = null,
    ascending: Boolean = true,
    onToggle: (Int) -> Unit = {},
    onFilter: (Boolean?) -> Unit = {},
    onSort: (Boolean) -> Unit = {},
    onAdd: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            val filterLabel = when (showDone) {
                null -> "Filter: All"
                true -> "Filter: Done"
                false -> "Filter: Not Done"
            }
            Button(onClick = { onFilter(showDone) }) {
                Text(filterLabel)
            }

            val sortLabel = if (ascending) "Sort: Asc" else "Sort: Desc"
            Button(onClick = { onSort(ascending) }) {
                Text(sortLabel)
            }

            Button(onClick = { onAdd() }) {
                Text("Add Task")
            }
        }

        LazyColumn {
            items(tasks, key = { it.id }) { task ->
                TaskRow(task = task, onToggle = onToggle)
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, onToggle: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onToggle(task.id) }
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "Due: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle(task.id) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Viikko1Theme {
        var allTasks by remember { mutableStateOf(mockTasks) }
        var showDone by remember { mutableStateOf<Boolean?>(null) }
        var ascending by remember { mutableStateOf(true) }

        fun computeDisplayed(): List<Task> {
            val filtered = filterByDone(allTasks, showDone)
            return sortByDueDate(filtered, ascending)
        }

        Surface {
            HomeScreen(
                tasks = computeDisplayed(),
                showDone = showDone,
                ascending = ascending,
                onToggle = { id ->
                    allTasks = toggleDone(allTasks, id)
                },
                onFilter = { current ->
                    showDone = when (current) {
                        null -> true
                        true -> false
                        false -> null
                    }
                },
                onSort = { currentAsc ->
                    ascending = !currentAsc
                },
                onAdd = {
                    val nextId = (allTasks.maxOfOrNull { it.id } ?: 0) + 1
                    val newTask = Task(
                        id = nextId,
                        title = "New Task $nextId",
                        description = "Task $nextId description",
                        priority = 1,
                        dueDate = "2026-01-30",
                        done = false
                    )
                    allTasks = addTask(allTasks, newTask)
                }
            )
        }
    }
}