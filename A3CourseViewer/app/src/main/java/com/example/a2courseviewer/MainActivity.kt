package com.example.a2courseviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a2courseviewer.ui.theme.A2CourseViewerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            A2CourseViewerTheme {
                CourseApp()
            }
        }
    }
}

@Composable
fun CourseApp(
    courseViewModel: CourseViewModel = viewModel(factory = CourseViewModel.Factory)
) {
    val courses by courseViewModel.courses.collectAsState()

    var selectedCourseId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    val selectedCourse = courses.firstOrNull { it.id == selectedCourseId }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (selectedCourse == null) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Text("+")
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Courses",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedCourse == null) {
                CourseList(
                    courses = courses,
                    onCourseClick = { course -> selectedCourseId = course.id },
                    modifier = Modifier.weight(1f)
                )
            } else {
                CourseDetails(
                    course = selectedCourse,
                    onDelete = {
                        courseViewModel.deleteCourse(selectedCourse)
                        selectedCourseId = null
                    },
                    onBack = { selectedCourseId = null },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showAddDialog) {
        AddCourseDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { department, number, location ->
                courseViewModel.addCourse(department, number, location)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CourseList(
    courses: List<Course>,
    onCourseClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(courses, key = { it.id }) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCourseClick(course) }
            ) {
                Text(
                    text = course.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CourseDetails(
    course: Course,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = "Course Details",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Department: ${course.department}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Number: ${course.number}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Location: ${course.location}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onDelete) {
                Text("Delete Course")
            }
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

@Composable
fun AddCourseDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var department by rememberSaveable { mutableStateOf("") }
    var number by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Course") },
        text = {
            Column {
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Department") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Course Number") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (department.isNotBlank() && number.isNotBlank() && location.isNotBlank()) {
                        onAdd(department.trim(), number.trim(), location.trim())
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}