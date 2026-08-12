package com.example.liveconversationtranslate.translation

import android.icu.text.Transliterator
import android.os.Build

class ReadingAssistantManager {

    suspend fun getReadingText(
        translatedText: String,
        translatedLanguageCode: String
    ): String {

        if (translatedText.isBlank()) {
            return ""
        }

        return try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                val transliterator =
                    Transliterator.getInstance("Any-Latin")

                transliterator.transliterate(translatedText)

            } else {

                translatedText
            }

        } catch (e: Exception) {

            translatedText
        }
    }
}