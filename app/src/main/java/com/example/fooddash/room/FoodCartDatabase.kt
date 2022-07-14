package com.example.fooddash.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fooddash.models.FoodModel

@Database(entities = [FoodModel::class], version = 2, exportSchema = false )
abstract class FoodCartDatabase : RoomDatabase() {

    /**firstly, connect the database to the Dao*/
    abstract fun foodDao(): FoodDao

    companion object{

        @Volatile
        private var INSTANCE: FoodCartDatabase? = null

/*
        fun getInstance(context: Context): FoodCartDatabase{

            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        FoodCartDatabase::class.java,
                        "foodCart_database"
                    )
                        //.fallbackToDestructiveMigration()
                        .build()

                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
*/

        fun getInstance(context: Context): FoodCartDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodCartDatabase::class.java, "user_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance

                return instance
            }
        }

    }

}