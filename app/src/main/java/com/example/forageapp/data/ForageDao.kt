package com.example.forageapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ForageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(forage:Forage)

    @Update
    suspend fun update(forage:Forage)

    @Delete
    suspend fun delete(forage:Forage)

    @Query("SELECT * FROM forage")
    fun getAllForages():Flow<List<Forage>>

    @Query("SELECT * FROM forage WHERE id = :id")
    fun getForage(id:Int):Flow<Forage>
}