package com.example.liveconversationtranslate.translation

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImageTextRecognizer(
    private val context: Context
) {

    private val recognizer =
        TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

    fun recognizeText(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        try {

            val image = InputImage.fromFilePath(
                context,
                imageUri
            )

            recognizer.process(image)
                .addOnSuccessListener { result ->

                    onSuccess(result.text)
                }
                .addOnFailureListener { exception ->

                    onFailure(exception)
                }

        } catch (exception: Exception) {

            onFailure(exception)
        }
    }

    fun close() {
        recognizer.close()
    }
}