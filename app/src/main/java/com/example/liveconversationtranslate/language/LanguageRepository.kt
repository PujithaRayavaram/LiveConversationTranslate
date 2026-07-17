

package com.example.liveconversationtranslate.language

import com.google.mlkit.nl.translate.TranslateLanguage

object LanguageRepository {

    val languages = listOf(
        Language("English", TranslateLanguage.ENGLISH),
        Language("Telugu", TranslateLanguage.TELUGU),
        Language("Hindi", TranslateLanguage.HINDI),
        Language("Tamil", TranslateLanguage.TAMIL),
        Language("Kannada", TranslateLanguage.KANNADA)
    )
}