package com.example.liveconversationtranslate


import com.example.liveconversationtranslate.translation.TranslatorManager
import com.example.liveconversationtranslate.service.SpeechService
import com.example.liveconversationtranslate.language.LanguageRepository
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
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

    private var speechText by mutableStateOf("")
    private var selectedSourceLanguage by mutableStateOf(LanguageRepository.languages[0])
    private var selectedTargetLanguage by mutableStateOf(LanguageRepository.languages[1])

    private var translatedText by mutableStateOf("")


    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechIntent: Intent
    private var isListening = false

    private val translatorManager = TranslatorManager()

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

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                selectedSourceLanguage.code
            )
            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                        true
            )
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                if (isListening) {
                    speechRecognizer.startListening(speechIntent)
                }
            }
            override fun onError(error: Int) {
                if (isListening && error == SpeechRecognizer.ERROR_NO_MATCH ||
                    error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT
                ) {
                    speechRecognizer.startListening(speechIntent)
                }
            }


            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.get(0)

                if(text !=null ){
                        speechText = text
                    android.util.Log.d("MAIN", "Recognized = $text")
                    android.util.Log.d("MAIN", "Calling TranslatorManager")
                    translatorManager.translate(
                        text = text,
                        sourceLanguage = selectedSourceLanguage.code,
                        targetLanguage = selectedTargetLanguage.code,
                        onSuccess = { translated ->

                            runOnUiThread {
                                translatedText = translated
                            }

                        },
                        onFailure = { exception ->

                            runOnUiThread {
                                translatedText = "Translation Failed"
                            }

                            android.util.Log.e(
                                "TRANSLATOR",
                                exception.toString()
                            )
                        }
                    )

                }
                if (isListening) {
                    speechRecognizer.startListening(speechIntent)
                }


            }

            override fun onPartialResults(partialResults: Bundle?) {

                val partialText = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0)

                if (partialText != null) {
                    speechText = partialText
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        setContent {


            LiveConversationTranslateTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        speechText = speechText,
                        translatedText = translatedText,
                        sourceLanguage = selectedSourceLanguage,
                        targetLanguage = selectedTargetLanguage,
                        languages = LanguageRepository.languages,
                        onSourceLanguageChange = { language ->
                            selectedSourceLanguage = language
                            speechIntent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE,language.code
                            )
                        },

                        onTargetLanguageChange = { language ->
                            selectedTargetLanguage = language
                        },

                        onSwapLanguages = {

                            val temp = selectedSourceLanguage
                            selectedSourceLanguage = selectedTargetLanguage
                            selectedTargetLanguage = temp

                            speechIntent.apply {
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE,
                                    selectedSourceLanguage.code
                                )
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                            }
                        },

                        onStartTranslation = {

                            isListening = true

                            startService(
                                Intent(this, SpeechService::class.java)
                            )

                            speechRecognizer.startListening(speechIntent)

                        },
                        onStopTranslation = {

                            isListening = false

                            speechRecognizer.stopListening()

                            stopService(
                                Intent(this, SpeechService::class.java)
                            )

                        },
                    )

                }
            }
        }
    }
}