package com.applications.fishcardroomandmvvm.ROOM.data

import android.content.Context
import androidx.room.*
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.TranslationTypeConverter
import com.applications.fishcardroomandmvvm.ROOM.model.Word

private const val DATABASE_NAME = "FishCard_database"

@Database(entities = [FishCardList::class, Word::class], version = 1,exportSchema = false)
@TypeConverters(TranslationTypeConverter::class)
abstract  class FishCardDatabase: RoomDatabase() {


    abstract fun wordListDao(): FishCardDao

    companion object{
        @Volatile
        private var INSTANCE: FishCardDatabase? = null

        fun getDatabase(context: Context): FishCardDatabase{

                val tempInstance = INSTANCE

            if(tempInstance != null){
                return  tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                FishCardDatabase::class.java,
                    DATABASE_NAME)
                    .build()

                INSTANCE = instance
                return  instance
            }

        }



    }

}