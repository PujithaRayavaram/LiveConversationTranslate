package com.example.liveconversationtranslate.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslatorManager {

    fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        android.util.Log.d("TRANSLATOR", "translate() called")
        android.util.Log.d("TRANSLATOR", "Input Text = $text")

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)
        android.util.Log.d(
            "TRANSLATOR",
            "Source = $sourceLanguage  Target = $targetLanguage"
        )

        val conditions = DownloadConditions.Builder()

            .build()
        android.util.Log.d("TRANSLATOR", "Downloading model...")

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                android.util.Log.d("TRANSLATOR", "Model downloaded")

                translator.translate(text)
                    .addOnSuccessListener { translatedText ->

                        android.util.Log.d("TRANSLATOR", "Translation = $translatedText")
                        onSuccess(translatedText)
                    }
                    .addOnFailureListener { exception ->

                        android.util.Log.e("TRANSLATOR", "Translate failed", exception)
                        onFailure(exception)
                    }

            }
            .addOnFailureListener { exception ->

                android.util.Log.e("TRANSLATOR", "Model download failed", exception)
                onFailure(exception)
            }
    }
}