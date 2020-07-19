package com.gaana.sbnri

import android.app.Application
import androidx.room.Room
import com.gaana.sbnri.database.OctokitDBManager
import com.gaana.sbnri.database.OctokitDatabase

class OctokitApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        OctokitDBManager.getInstance().initDatabase(this)
    }

    init {
        instanceObj = this
    }

    companion object {
        var instanceObj: OctokitApplication? = null

        fun getInstance(): OctokitApplication {
            return instanceObj!!
        }
    }
}