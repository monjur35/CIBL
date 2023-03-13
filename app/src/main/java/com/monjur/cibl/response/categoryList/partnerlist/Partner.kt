package com.monjur.cibl.response.categoryList.partnerlist

data class Partner(
    val address: String,
    val category_name: String,
    val details: String,
    val discountPercentage: String,
    val discount_id: String,
    val fromDate: String,
    val image: String,
    val mobile: String,
    val offerType: String,
    val parent_id: Any,
    val parent_name: Any,
    val partner_name: String,
    val pc_id: String,
    val toDate: String,
    val type: String,
    val url: String
)