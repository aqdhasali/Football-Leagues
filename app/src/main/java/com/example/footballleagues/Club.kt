package com.example.footballleagues

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Club (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String?,
    var shortName: String?,
    var alternateTeam: String?,
    var formedYear: Int = 0,
    var league: String?,
    var stadium: String?,
    var keywords: String?,
    var stadiumThumb: String?,
    var stadiumCapacity: Int = 0,
    var teamWebsite: String?,
    var teamJersey: String?,
    var teamLogo: String?
)