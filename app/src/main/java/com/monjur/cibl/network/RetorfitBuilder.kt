package com.monjur.cibl.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

object RetorfitBuilder {

    val baseUrl="http://27.147.135.164/eblproxy/index.php/nbl_api/api/"


    var t: Timestamp = Timestamp(System.currentTimeMillis())
    var gson: Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
        .create()
    private var httpClient=
        OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60, TimeUnit.SECONDS).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient).build()
    }

    val API_SERVICE: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}