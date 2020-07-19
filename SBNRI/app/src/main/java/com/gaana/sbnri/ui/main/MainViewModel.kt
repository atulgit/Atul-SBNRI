package com.gaana.sbnri.ui.main

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaana.sbnri.data.model.*
import com.gaana.sbnri.database.OctokitDBManager
import com.gaana.sbnri.database.OctokitDao
import com.gaana.sbnri.database.OctokitEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainViewModel : ViewModel() {

    var octokitList: MutableLiveData<List<OctokitModel>> = MutableLiveData<List<OctokitModel>>()
    var errorData: MutableLiveData<String> = MutableLiveData()

    fun loadFromDatabase() {
        var octokitDao: OctokitDao =
            OctokitDBManager.getInstance().getOctokitDatabase().octokitDao()

        AsyncTask.execute {
            var list = octokitDao.getAllOctokitItems()
            if (list.size > 0) octokitList.postValue(fromEntity(list))
        }
    }

    fun loadFromNetwork(page: Integer) {

        Log.d("loadFromNetwork", page.toString())

        var octokitDao: OctokitDao =
            OctokitDBManager.getInstance().getOctokitDatabase().octokitDao()

        var retrofit: Retrofit = OctokitRepo.initOctokit();
        var octokitAPI: OctokitAPI = retrofit.create(OctokitAPI::class.java)
        if(page > 5) return;

        octokitAPI.loadOctokitList(page, Integer(5))
            .enqueue(object : Callback<List<OctokitModel>> {
                override fun onResponse(
                    call: Call<List<OctokitModel>>,
                    response: Response<List<OctokitModel>>
                ) {

                    var octokit: List<OctokitModel>? = response.body()
                    var octokitEntityItems: ArrayList<OctokitEntity> = ArrayList()
                    if (response.isSuccessful) {
                        octokitList.postValue(octokit)
                    } else {
                        errorData.postValue(response.errorBody().toString())
                        return;
                    }

                    for (item in octokit as ArrayList) {
                        octokitEntityItems.add(
                            OctokitEntity(
                                item.id,
                                item.name,
                                item.description,
                                item.open_issues_count,
                                item.license?.key,
                                item.license?.name,
                                item.license?.spdx_id,
                                item.permissions?.push,
                                item.permissions?.pull,
                                item.permissions?.admin
                            )
                        )
                    }

                    AsyncTask.execute {
                        var list = octokitDao.getAllOctokitItems()
                        var listToAdd: ArrayList<OctokitEntity> = ArrayList<OctokitEntity>()
                        for (networkItem in octokit) {
                            var itemFound: Boolean = false
                            for (dbItem in list) {
                                if (networkItem.id.equals(dbItem.id)) {
                                    itemFound = true
                                    break
                                }
                            }

                            if (!itemFound) listToAdd.add(
                                OctokitEntity(
                                    networkItem.id,
                                    networkItem.name,
                                    networkItem.description,
                                    networkItem.open_issues_count,
                                    networkItem.license?.key,
                                    networkItem.license?.name,
                                    networkItem.license?.spdx_id,
                                    networkItem.permissions?.push,
                                    networkItem.permissions?.pull,
                                    networkItem.permissions?.admin
                                )
                            )
                        }

                        octokitDao.insertOctokitItems(listToAdd)
                    }
                }


                override fun onFailure(call: Call<List<OctokitModel>>, t: Throwable) {
                    var data = "";
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

    fun fromEntity(itemList: List<OctokitEntity>): List<OctokitModel> {
        var octokitItems: ArrayList<OctokitModel> = ArrayList<OctokitModel>()
        for (item in itemList) {
            octokitItems.add(
                OctokitModel(
                    item.id,
                    item.openIssuesCount,
                    License(item.name, item.name, item.name),
                    Permission(item.adminPermission, item.pushPermission, item.pullPermission),
                    item.name,
                    item.description
                )
            )
        }

        return octokitItems
    }
}
