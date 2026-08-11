package com.example.liveconversationtranslate.translation

import android.icu.text.Transliterator

class ReadingAssistantManager {

    suspend fun getReadingText(
        translatedText: String,
        translatedLanguageCode: String,
        readingLanguageCode: String
    ): String {

        if (readingLanguageCode != "en")
            return translatedText

        return try {

            val transliterator =
                Transliterator.getInstance("Any-Latin")

            transliterator.transliterate(translatedText)

        } catch (e: Exception) {

            translatedText
        }
    }
}