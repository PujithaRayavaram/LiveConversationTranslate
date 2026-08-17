package com.example.liveconversationtranslate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
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
)

{

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
fun SelectedImage(
    imageUri: Uri?
) {
    if (imageUri == null) return

    val context = androidx.compose.ui.platform.LocalContext.current

    val bitmap = remember(imageUri) {
        context.contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Selected image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    speechText: String,
    textInput: String,
    imageUri: Uri?,
    onTextInputChange: (String) -> Unit,
    onTranslateText: () -> Unit,
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

    onSpeakPronunciation: () -> Unit,
    onGalleryClick: () ->Unit
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

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "⌨️ Text Input",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = textInput,
                    onValueChange = onTextInputChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Enter text")
                    },
                    placeholder = {
                        Text("Type something to translate...")
                    },
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onTranslateText,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🌐 Translate Text")
                }
            }
        }

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


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "🖼️ Image Translation",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = {
                            // Camera will be connected in the next step
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("📷 Camera")
                    }

                    Button(
                        onClick = onGalleryClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🖼️ Gallery")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                SelectedImage(
                    imageUri = imageUri 
                )


            }
        }

        Spacer(modifier = Modifier.height(20.dp))
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