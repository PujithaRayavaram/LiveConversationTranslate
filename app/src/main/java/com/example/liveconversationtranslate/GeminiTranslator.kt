package com.example.liveconversationtranslate.translation

import com.google.genai.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiTranslator {

    private val client = Client(
        apiKey = ""
    )

    suspend fun translate(
        text: String,
        targetLanguage: String
    ): String {

        val prompt = """
            Translate the following text to $targetLanguage.
            Return ONLY the translated text.
            
            Text:
            $text
        """.trimIndent()

        return withContext(Dispatchers.IO) {

            val response = client.models.generateContent(
                model = "gemini-2.5-flash",
                contents = prompt
            )

            response.text ?: "Translation Failed"
        }
    }
}