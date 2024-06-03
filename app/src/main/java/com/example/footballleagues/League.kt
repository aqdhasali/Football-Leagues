package com.example.footballleagues

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class League (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String?,
    var sport: String?,
    var alternateName: String?
)