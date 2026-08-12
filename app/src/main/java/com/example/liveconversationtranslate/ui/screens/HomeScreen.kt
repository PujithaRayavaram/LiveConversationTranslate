package com.example.liveconversationtranslate.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liveconversationtranslate.language.Language

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
            label = {
                Text(label)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
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

    readingText: String,

    sourceLanguage: Language,
    targetLanguage: Language,
    languages: List<Language>,

    onSourceLanguageChange: (Language) -> Unit,
    onTargetLanguageChange: (Language) -> Unit,
    onSwapLanguages: () -> Unit,

    onStartTranslation: () -> Unit,
    onStopTranslation: () -> Unit,

    onSpeakPronunciation: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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

        // -----------------------------
        // TRANSLATION LANGUAGES
        // -----------------------------

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

        // -----------------------------
        // SPEECH + TRANSLATION
        // -----------------------------

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

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "🌍 Translation",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = translatedText,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                // -----------------------------
                // READING ASSISTANT
                // -----------------------------

                Text(
                    text = "📖 Reading Assistant",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))



                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "How to read",
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(
                        onClick = onSpeakPronunciation
                    ) {

                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak reading"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = readingText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // -----------------------------
        // START
        // -----------------------------

        Button(
            onClick = onStartTranslation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎤 Start Translation")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // -----------------------------
        // STOP
        // -----------------------------

        Button(
            onClick = onStopTranslation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⏹ Stop")
        }
    }
}