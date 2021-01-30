package com.applications.fishcardroomandmvvm

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import java.util.*
import kotlin.collections.HashSet

private const val TAG = "SpeechManager"

class SpeechManager(val context: Context) :
    TextToSpeech(context, TextToSpeech.OnInitListener { status ->
        if (status != SUCCESS) Log.e(TAG, "status in SpeechManager is not SUCCESS")
    }) {

    private var language: Int = LANG_MISSING_DATA


    fun setLanguage(languageId: String) {

        language = when (languageId) {

            LanguageManager.ID_ENGLISH -> setLanguage(Locale.ENGLISH)
            LanguageManager.ID_GERMANY -> setLanguage(Locale.GERMAN)
            LanguageManager.ID_ITALIAN -> setLanguage(Locale.ITALIAN)
            LanguageManager.ID_POLISH -> LANG_NOT_SUPPORTED

            else -> LANG_NOT_SUPPORTED
        }

    }

    fun sayText(text: String) {

        when (language) {
            LANG_MISSING_DATA -> {

                throw error("you must set language")

            }
            LANG_NOT_SUPPORTED -> {

                Toast.makeText(context, "Language is not supported", Toast.LENGTH_SHORT).show()

            }
            else -> {
                speak(text, QUEUE_FLUSH, null)
            }
        }

    }

}