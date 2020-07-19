package com.gaana.sbnri.data.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OctokitRepo {

    companion object {
        fun initOctokit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}