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

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                android.util.Log.d("TRANSLATOR","Model Downloaded Successfully")

                android.util.Log.d("TRANSLATOR","Starting Translation")
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        android.util.Log.d("TRANSLATOR","Translated = $translatedText")
                        onSuccess(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        android.util.Log.e("TRANSLATOR",exception.toString())
                        onFailure(exception)
                    }

            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}