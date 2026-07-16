package com.example.liveconversationtranslate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent

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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private var speechText by mutableStateOf("Waiting for speech...")

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                println("Microphone Permission Granted")
            } else {
                println("Microphone Permission Denied")
            }
        }

    private val speechRecognizerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {

                val spokenText = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.get(0)
                if (spokenText != null) {
                    speechText = spokenText
                    android.util.Log.d("SpeechDebug","Recognized: $spokenText")
                }


            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        setContent {


            LiveConversationTranslateTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        speechText = speechText,
                        onStartTranslation = {

                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

                            intent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )

                            intent.putExtra(
                                RecognizerIntent.EXTRA_PROMPT,
                                "Speak now..."
                            )

                            speechRecognizerLauncher.launch(intent)

                        }
                    )

                }
            }
        }
    }
}