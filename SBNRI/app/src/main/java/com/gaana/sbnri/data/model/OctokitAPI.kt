package com.gaana.sbnri.data.model

import androidx.annotation.IntegerRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OctokitAPI {

    @GET("orgs/octokit/repos")
    fun loadOctokitList(@Query("page") page: Integer, @Query("per_page") per_page: Integer): Call<List<OctokitModel>>
}