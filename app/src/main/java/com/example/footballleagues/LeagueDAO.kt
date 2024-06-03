package com.example.footballleagues

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface LeagueDAO {
    // Query to get the list of the available leagues
    @Query("select * from league")
    suspend fun getAll(): List<League>

    // To insert the leagues to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg league: League)

}