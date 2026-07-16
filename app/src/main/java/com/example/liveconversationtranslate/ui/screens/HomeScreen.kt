package com.example.liveconversationtranslate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    speechText: String,
    onStartTranslation: () ->Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🌍 Live Conversation Translate",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Understand any language in real time"
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = speechText,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
                // Speech Recognition will start here
                onClick = onStartTranslation
        ) {
            Text("🎤 Start Translation")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                // Stop Listening
            }
        ) {
            Text("Stop")
        }

    }
}