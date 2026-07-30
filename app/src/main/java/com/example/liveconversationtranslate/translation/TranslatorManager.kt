package com.example.liveconversationtranslate.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslatorManager {

    private var translator: Translator? = null

    fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        if (sourceLanguage == targetLanguage) {
            onSuccess(text)
            return
        }

        translator?.close()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        translator = Translation.getClient(options)

        translator!!
            .downloadModelIfNeeded(
                DownloadConditions.Builder().build()
            )
            .addOnSuccessListener {

                translator!!
                    .translate(text)
                    .addOnSuccessListener { translatedText ->
                        onSuccess(translatedText)
                    }
                    .addOnFailureListener {
                        onFailure(it)
                    }

            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun close() {
        translator?.close()
    }
}