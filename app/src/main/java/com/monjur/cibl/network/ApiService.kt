package com.monjur.cibl.network

import com.monjur.cibl.response.categoryList.CategoryList
import com.monjur.cibl.response.categoryList.partnerlist.PartnerList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {


    @Headers("Accept: application/json")
    @GET("get_categories")
    suspend fun getCategory(
        @Query("type") type: String
    ): Response<CategoryList>


  @Headers("Accept: application/json")
    @GET("get_categories")
    suspend fun getCategory1(
        @Query("type") type: String,
        @Query("parent_id") parentId: String,
    ): Response<CategoryList>

 @Headers("Accept: application/json")
    @GET("get_discount_partners")
    suspend fun getPartnerList(
        @Query("pc_id") pc_id: String,
    ): Response<PartnerList>


    //type=benefit
}