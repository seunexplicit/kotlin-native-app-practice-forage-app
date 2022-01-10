package com.example.forageapp

import android.app.Application
import com.example.forageapp.data.ForageRoomDatabase

class ForageApplication:Application() {
    val database:ForageRoomDatabase by lazy{
        ForageRoomDatabase.getDatabase(this)
    }
}