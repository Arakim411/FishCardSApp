package com.applications.fishcardroomandmvvm

import android.content.Context
import android.content.SharedPreferences

const val FISH_LIST_EXTRA = "FISH_LIST_EXTRA"
const val LEARN_EXTRA_TYPE = "LEARN_EXTRA_TYPE"
const val LEARN_EXTRA_LIST_ID = "LEARN_LIST_ID"

//LearnActivityManager const

private const val LEARN_WORDS_SHARED_PREFERENCES = "LEARN_SHARED"
private const val SPELLING_WORD_SHARED_PREFERENCES = "SPELLING_SHARED"


abstract class Manager() {


    companion object {


        fun getLearnManager(context: Context): WordsManager = WordsManager(context)
        fun getSpellingManager(context: Context): SpellingManager = SpellingManager(context)

//        @Volatile
//        private var manager: Manager.WordsManager? = null
//
//        fun getLearnActivityManager(context: Context): WordsManager {
//
//            val tempInstance = manager
//
//            return if (tempInstance != null) {
//                tempInstance
//            } else {
//                val instance = WordsManager(context)
//                manager = instance
//                instance
//            }
//        }

    }


    class WordsManager(context: Context) : Manager() {
        //this object helps to store options related to LearnActivity with type Words
        // keeps value of options which user set
        //for every option which user can change i use different function to save value
        //it could be done by other way by this approach is later easier to manager
        // and has also disadvantage for example when we want more options which can user change. we must define function for them here.

        //options
        val showWithTranslate = "show_with_translate"
        private val showDefValue = false

        val saveData = "save_data" // defines whether user must tick that he know or don't know word
        private val saveDefValue = false

        private val randomList = "random_list" // list is in order  or in random order
        private val randomDefValue = false


        val showStatistics = "show_statistics"
        private val statisticsDefValue = false

        //shared preferences
        private val sharedPreferences = context.getSharedPreferences(
            LEARN_WORDS_SHARED_PREFERENCES, Context.MODE_PRIVATE
        )

        private val editor = sharedPreferences.edit()

        fun setListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }

        //SET
        // it could be made by one function for example setData( (on of Keys Above), boolean)
        // but i choose this approach hose this approach
        fun setShowWithTranslation(boolean: Boolean) =
            editor.putBoolean(showWithTranslate, boolean).commit()

        fun setSaveData(boolean: Boolean) = editor.putBoolean(saveData, boolean).commit()

        fun setRandomList(boolean: Boolean) = editor.putBoolean(randomList, boolean).commit()


        fun setShowStatistics(boolean: Boolean) =
            editor.putBoolean(showStatistics, boolean).commit()


        //GET
        fun getShowWithTranslate(): Boolean =
            sharedPreferences.getBoolean(showWithTranslate, showDefValue)

        fun getSaveData(): Boolean = sharedPreferences.getBoolean(saveData, saveDefValue)

        fun getRandomList(): Boolean = sharedPreferences.getBoolean(randomList, randomDefValue)

        fun getShowStatistics(): Boolean =
            sharedPreferences.getBoolean(showStatistics, statisticsDefValue)

        fun clear() = editor.clear().apply()

    }

    class SpellingManager(context: Context) : Manager() {

        val showHint = "showHint"
        private val showHintDefValue = true

        private val randomList = "randomList"
        private val randomListDefValue = false

        fun setListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }

        private val sharedPreferences =
            context.getSharedPreferences(SPELLING_WORD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        private val editor = sharedPreferences.edit()

        fun setShowHint(boolean: Boolean) {
            editor.putBoolean(showHint, boolean).commit()
        }

        fun setRandomList(boolean: Boolean) {
            editor.putBoolean(randomList, boolean).commit()
        }

        fun getShowHint(): Boolean = sharedPreferences.getBoolean(showHint, showHintDefValue)

        fun getRandomList(): Boolean = sharedPreferences.getBoolean(randomList, randomListDefValue)

        fun clear() = editor.clear().apply()

    }

}

