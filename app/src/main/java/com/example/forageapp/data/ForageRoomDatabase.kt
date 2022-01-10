package com.example.forageapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Forage::class], version = 1, exportSchema = false)
abstract class ForageRoomDatabase:RoomDatabase() {

    abstract  fun forageDao():ForageDao

    companion object {
        @Volatile
        private var INSTANCE:ForageRoomDatabase? = null

        fun getDatabase(context: Context):ForageRoomDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    ForageRoomDatabase::class.java,
                    "forage_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return  instance
            }
        }
    }
}