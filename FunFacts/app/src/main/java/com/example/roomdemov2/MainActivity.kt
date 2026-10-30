package com.example.roomdemov2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomdemov2.room.FunFact
import com.example.roomdemov2.ui.theme.RoomDemoV2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as FunFactApp).repository

        setContent {
            RoomDemoV2Theme {
                val vm: FunFactViewModel = viewModel(
                    factory = FunFactViewModelFactory(repository)
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FunFactScreen(vm, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FunFactScreen(vm: FunFactViewModel, modifier: Modifier = Modifier) {
    val facts by vm.facts.collectAsStateWithLifecycle()
    val query by vm.query.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Fun Facts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = { vm.fetchNewFact() },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("Get New Fun Fact")
            }
        }

        error?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        OutlinedTextField(
            value = query,
            onValueChange = vm::onQueryChange,
            label = { Text("Search saved facts") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        if (facts.isEmpty()) {
            Text(
                if (query.isBlank()) "No facts yet. Tap the button to fetch one!"
                else "No saved facts match \"$query\".",
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(facts, key = { it.id }) { FactCard(it) }
            }
        }
    }
}

@Composable
fun FactCard(fact: FunFact) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(fact.text, style = MaterialTheme.typography.bodyLarge)
            if (fact.source.isNotBlank()) {
                Row(Modifier.padding(top = 6.dp)) {
                    Text(
                        "Source: ${fact.source}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
