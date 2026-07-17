package com.example.liveconversationtranslate.ui.screens

import com.example.liveconversationtranslate.language.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    label: String,
    selectedLanguage: Language,
    languages: List<Language>,
    onLanguageSelected: (Language) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            value = selectedLanguage.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            languages.forEach { language ->

                DropdownMenuItem(
                    text = {
                        Text(language.name)
                    },
                    onClick = {

                        onLanguageSelected(language)
                        expanded = false

                    }
                )

            }

        }

    }

}
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    speechText: String,
    translatedText: String,
    sourceLanguage: Language,
    targetLanguage: Language,
    languages: List<Language>,
    onSourceLanguageChange:
    (Language) ->Unit,
    onTargetLanguageChange:
    (Language) ->Unit,
    onSwapLanguages: () -> Unit,
    onStartTranslation: () ->Unit,
    onStopTranslation: () -> Unit
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🌍 Live Conversation Translate",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Understand any language in real time"
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "🌍 Translation Languages",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LanguageDropdown(
                    label = "Source Language",
                    selectedLanguage = sourceLanguage,
                    languages = languages,
                    onLanguageSelected = onSourceLanguageChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                LanguageDropdown(
                    label = "Target Language",
                    selectedLanguage = targetLanguage,
                    languages = languages,
                    onLanguageSelected = onTargetLanguageChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSwapLanguages
                ) {
                    Text("🔄 Swap Languages")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "🗣 Recognized Speech",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = speechText,
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = "🌍 Translation",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = translatedText,
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
                // Speech Recognition will start here
                onClick = onStartTranslation
        ) {
            Text("🎤 Start Translation")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onStopTranslation
                // Stop Listening

        ) {
            Text("Stop")
        }

    }
}