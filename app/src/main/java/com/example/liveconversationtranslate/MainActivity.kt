package com.example.liveconversationtranslate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.liveconversationtranslate.ui.screens.HomeScreen
import com.example.liveconversationtranslate.ui.theme.LiveConversationTranslateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LiveConversationTranslateTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}