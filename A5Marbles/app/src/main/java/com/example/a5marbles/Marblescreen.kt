package com.example.a5marbles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MarbleScreen(viewModel: MarbleViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!state.sensorAvailable) {
                Text(
                    "No gravity sensor or accelerometer on this device",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val maxX = maxWidth.value - MarbleViewModel.MARBLE_SIZE_DP
                val maxY = maxHeight.value - MarbleViewModel.MARBLE_SIZE_DP

                // tell the ViewModel how far the marble may travel
                LaunchedEffect(maxX, maxY) { viewModel.setBounds(maxX, maxY) }

                // offset BEFORE size/background, as the assignment suggests
                Box(
                    modifier = Modifier
                        .offset(x = state.x.dp, y = state.y.dp)
                        .size(MarbleViewModel.MARBLE_SIZE_DP.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }
    }
}