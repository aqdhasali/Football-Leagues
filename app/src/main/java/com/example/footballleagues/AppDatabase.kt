package com.example.footballleagues

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [League::class,Club::class], version = 1)
abstract class AppDatabase :RoomDatabase() {
    abstract fun getLeagueDao(): LeagueDAO
    abstract fun getClubDao(): ClubDAO
}