package com.example.liveconversationtranslate.pronunciation

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class PronunciationManager(context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech = TextToSpeech(context, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
        }
    }

    fun speak(text: String, languageCode: String) {

        if (!isReady) return

        val locale = Locale.forLanguageTag(languageCode)

        textToSpeech.language = locale

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "translation"
        )
    }

    fun getPronunciation(
        translatedText: String,
        targetLanguage: String
    ): String {
        return translatedText
    }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}