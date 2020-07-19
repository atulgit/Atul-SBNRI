package com.gaana.sbnri.database

import android.content.Context
import androidx.room.Room

class OctokitDBManager {

    var db: OctokitDatabase? = null

    companion object {
        private var octokitDBManager: OctokitDBManager? = null

        fun getInstance(): OctokitDBManager {
            if (octokitDBManager == null) {
                synchronized(this) {
                    if (octokitDBManager == null) {
                        octokitDBManager = OctokitDBManager()
                    }
                }
            }

            return octokitDBManager!!;
        }
    }

    fun getOctokitDatabase(): OctokitDatabase {
        return db!!;
    }

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(
            context,
            OctokitDatabase::class.java, "octokit-list.db"
        ).build() as OctokitDatabase
    }
}