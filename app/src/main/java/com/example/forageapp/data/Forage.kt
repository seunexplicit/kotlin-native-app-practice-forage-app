package com.example.forageapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forage")
data class Forage(
   @PrimaryKey(autoGenerate = true) val id:Int = 0,
   @ColumnInfo(name="forage_name") val name:String,
   @ColumnInfo(name="forage_location") val location:String,
   @ColumnInfo(name="forage_note") val note:String,
   @ColumnInfo(name="forage_is_season") val isSeason:Boolean
)