package com.example.liveconversationtranslate


import com.example.liveconversationtranslate.translation.ImageTextRecognizer
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.liveconversationtranslate.language.LanguageRepository
import com.example.liveconversationtranslate.pronunciation.PronunciationManager
import com.example.liveconversationtranslate.service.SpeechService
import com.example.liveconversationtranslate.translation.ReadingAssistantManager
import com.example.liveconversationtranslate.translation.TranslatorManager
import com.example.liveconversationtranslate.ui.screens.HomeScreen
import com.example.liveconversationtranslate.ui.theme.LiveConversationTranslateTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var speechText by mutableStateOf("")
    private var textInput by mutableStateOf("")
    private var selectedSourceLanguage by mutableStateOf(LanguageRepository.languages[0])
    private var selectedTargetLanguage by mutableStateOf(LanguageRepository.languages[1])


    private var translatedText by mutableStateOf("")
    private var readingText by mutableStateOf("")

    private var selectedImageUri by mutableStateOf<android.net.Uri?>(null)


    private lateinit var pronunciationManager: PronunciationManager


    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechIntent: Intent
    private var isListening = false
    private val handler = Handler(Looper.getMainLooper())

    private val stopRunnable = Runnable {
        stopTranslation()
    }

    private val translatorManager = TranslatorManager()
    private lateinit var imageTextRecognizer: ImageTextRecognizer


    private val readingAssistantManager = ReadingAssistantManager()

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                selectedImageUri = uri

                imageTextRecognizer.recognizeText(
                    imageUri = uri,

                    onSuccess = { text ->

                        runOnUiThread {

                        }

                        android.util.Log.d(
                            "IMAGE_OCR",
                            "Extracted Text = $text"
                        )
                    },

                    onFailure = { exception ->

                        runOnUiThread {


                        }

                        android.util.Log.e(
                            "IMAGE_OCR",
                            exception.toString()
                        )
                    }
                )
            }
        }

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

    private fun translateTextInput() {

        if (textInput.isBlank()) {
            return
        }

        translatorManager.translate(
            text = textInput,
            sourceLanguage = selectedSourceLanguage.code,
            targetLanguage = selectedTargetLanguage.code,

            onSuccess = { translated ->

                lifecycleScope.launch {

                    val reading =
                        readingAssistantManager.getReadingText(
                            translatedText = translated,
                            translatedLanguageCode = selectedTargetLanguage.code
                        )

                    runOnUiThread {

                        translatedText = translated
                        readingText = reading
                    }
                }
            },

            onFailure = { exception ->

                runOnUiThread {
                    translatedText = "Translation Failed"
                    readingText = ""
                }

                android.util.Log.e(
                    "TEXT_TRANSLATOR",
                    exception.toString()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageTextRecognizer = ImageTextRecognizer(this)

        pronunciationManager = PronunciationManager(this)
        enableEdgeToEdge()

        val startFromTile = intent.getBooleanExtra(
            "START_TRANSLATION",
            false
        )

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
                if (
                    isListening &&
                    (
                            error == SpeechRecognizer.ERROR_NO_MATCH ||
                                    error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT
                            )
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
                    handler.removeCallbacks(stopRunnable)
                    handler.postDelayed(stopRunnable, 15000)
                    android.util.Log.d("MAIN", "Recognized = $text")
                    android.util.Log.d("MAIN", "Calling TranslatorManager")
                    translatorManager.translate(
                        text = text,
                        sourceLanguage = selectedSourceLanguage.code,
                        targetLanguage = selectedTargetLanguage.code,
                        onSuccess = { translated ->

                            lifecycleScope.launch {

                                val reading = readingAssistantManager.getReadingText(
                                    translatedText = translated,
                                    translatedLanguageCode = selectedTargetLanguage.code
                                )

                                runOnUiThread {

                                    translatedText = translated
                                    readingText = reading

                                }
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

                    handler.removeCallbacks(stopRunnable)
                    handler.postDelayed(stopRunnable, 15000)
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
                        textInput = textInput,
                        imageUri = selectedImageUri,

                        onTextInputChange = { text ->
                            textInput = text
                        },

                        onTranslateText = {
                            translateTextInput()
                        },
                        translatedText = translatedText,
                        readingText= readingText,
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

                            speechIntent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE,
                                selectedSourceLanguage.code
                            )
                        },

                        onStartTranslation = {
                            startTranslation()
                        },

                        onStopTranslation = {
                            stopTranslation()
                        },
                        onSpeakPronunciation = {
                            pronunciationManager.speak(
                                translatedText,
                               "en"
                            )
                        },
                        onGalleryClick = {
                            galleryLauncher.launch("image/*")
                        },

                    )

                }
            }
        }

        if (startFromTile) {
            startTranslation()
        }

    }

    private fun startTranslation() {

        isListening = true

        startService(
            Intent(this, SpeechService::class.java)
        )

        handler.removeCallbacks(stopRunnable)
        handler.postDelayed(stopRunnable, 15000)

        speechRecognizer.startListening(speechIntent)
    }

    private fun stopTranslation() {

        isListening = false

        handler.removeCallbacks(stopRunnable)

        speechRecognizer.stopListening()

        stopService(
            Intent(this, SpeechService::class.java)
        )

        android.util.Log.d("MAIN", "Translation Stopped")
    }

    override fun onDestroy() {
        super.onDestroy()

        speechRecognizer.destroy()
        translatorManager.close()
        pronunciationManager.shutdown()
        imageTextRecognizer.close()
        handler.removeCallbacks(stopRunnable)
    }
}