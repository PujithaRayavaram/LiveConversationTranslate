
package com.example.liveconversationtranslate.reading

class ReadingTransliterator {

    fun convert(
        text: String,
        languageCode: String
    ): String {

        return when(languageCode){

            "te" -> transliterateTelugu(text)

            else -> text
        }
    }

    private fun transliterateTelugu(text:String):String{

        return text
            .replace("అ","a")
            .replace("ఆ","aa")
            .replace("ఇ","i")
            .replace("ఈ","ee")
            .replace("ఉ","u")
            .replace("ఊ","oo")
            .replace("ఎ","e")
            .replace("ఏ","ae")
            .replace("ఐ","ai")
            .replace("ఒ","o")
            .replace("ఓ","oa")

            .replace("న","na")
            .replace("మ","ma")
            .replace("య","ya")
            .replace("ల","la")
            .replace("వ","va")
    }
}