package com.gaana.sbnri.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OctokitDao {

    @Query("SELECT * FROM octokit")
    fun getAllOctokitItems(): List<OctokitEntity>

    @Insert
    fun insertOctokitItems(octokitItems: ArrayList<OctokitEntity>)
}