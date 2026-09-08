package com.example.lecture4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CombineText()
        }
    }
}

@Composable
fun CombineText() {
    var firstText by remember { mutableStateOf("") }
    var secondText by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        OutlinedTextField(
            value = firstText,
            onValueChange = { firstText = it },
            label = { Text("First Text") }
        )

        OutlinedTextField(
            value = secondText,
            onValueChange = { secondText = it },
            label = { Text("Second Text") }
        )

        Button(
            onClick = {
                result = firstText + secondText
            }
        ) {
            Text("Combine")
        }

        Text(text = result)
    }
}
