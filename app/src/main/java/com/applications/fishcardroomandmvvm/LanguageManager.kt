package com.applications.fishcardroomandmvvm

import android.content.Context


class LanguageManager  constructor(val context: Context) {

    private var idToName: Map<String,String>
    private var nameToId: Map<String,String>
    private var idToDrawableResources: Map<String,Int>

    var languagesNames:List<String> = context.resources.getStringArray(R.array.languages).toList()


    init {

        idToName  = languagesId.zip(languagesNames).toMap()
        nameToId = languagesNames.zip(languagesId).toMap()
        idToDrawableResources = languagesId.zip(languagesDrawable).toMap()


    }

    companion object {
        const val ID_ENGLISH = "ID_ENGLISH"
        const val ID_POLISH = "ID_POLISH"
        const val ID_GERMANY = "ID_GERMANY"
        const val ID_ITALIAN = "ID_ITALIAN"

        // in two tables, the data should be added in the same order
         val languagesId = arrayListOf(ID_ENGLISH, ID_POLISH, ID_GERMANY, ID_ITALIAN)
        val languagesDrawable = arrayListOf(R.drawable.ic_united_kingdom,R.drawable.ic_poland,R.drawable.ic_germany,R.drawable.ic_italy)

    }

    fun getLanguageId(name: String): String{

       return nameToId[name] ?: throw Error("No such LanguageName: $name in available language")
    }

    fun getLanguageName(id: String): String{

        return idToName[id] ?: throw Error("No such LanguageId: $id in available languages")
    }

    fun getLanguageDrawableResources(id: String): Int{

        return idToDrawableResources[id] ?: throw Error("No such drawable for id: $id in available language drawables")
    }

}