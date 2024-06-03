package com.example.footballleagues

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ClubDAO {
    // Query to get the list of all the clubs
    @Query("select * from club")
    suspend fun getAll():List<Club>

    // To inset the retrieved clubs to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg club: Club)

    //Query to get the list of clubs by clubNames
    @Query("select name from club where name like '%' || :text || '%' ")
    suspend fun getClubByName(text: String): List<String>

    @Query("select name from club where league like '%' || :text || '%' ")
    suspend fun getClubByLeagueName(text: String): List<String>

    @Query ("select teamLogo from club where name like '%' || :clubName || '%' ")
    suspend fun getTeamLogo(clubName: String): String
}