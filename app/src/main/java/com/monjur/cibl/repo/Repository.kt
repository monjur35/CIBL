package com.monjur.cibl.repo

import com.monjur.cibl.network.RetorfitBuilder
import retrofit2.Retrofit

class Repository {
    val apiService= RetorfitBuilder.API_SERVICE

    suspend fun getCategoryList(type:String)=apiService.getCategory(type)
    suspend fun getCategoryList1(type:String,parentId:String)=apiService.getCategory1(type,parentId)
    suspend fun getPartnerList(pcId:String)=apiService.getPartnerList(pcId)
}