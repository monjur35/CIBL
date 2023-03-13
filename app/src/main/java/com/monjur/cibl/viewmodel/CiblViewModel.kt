package com.monjur.cibl.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monjur.cibl.repo.Repository
import com.monjur.cibl.response.categoryList.CategoryList
import com.monjur.cibl.response.categoryList.partnerlist.PartnerList
import kotlinx.coroutines.*

class CiblViewModel :ViewModel() {

    val repository=Repository()


    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val loading = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Poor Connection")
        Log.e("TAG401", "Error exceptionHandler: ${throwable.localizedMessage}")
        Log.e("TAG401", "Error exceptionHandler: ${throwable.printStackTrace()}")
    }

    private fun onError(s: String) {
        Log.e("TAG401", "Error exceptionHandler: $s")
    }

     fun getCategories(type:String): MutableLiveData<CategoryList> {
        val categoryListLiveData=MutableLiveData<CategoryList>()
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=repository.getCategoryList(type)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    categoryListLiveData.postValue(response.body())
                }else{
                    onError(response.message())
                }
            }
        }

        return categoryListLiveData
    }

    fun getCategories1(type:String,parentId:String): MutableLiveData<CategoryList> {
        val categoryListLiveData=MutableLiveData<CategoryList>()
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=repository.getCategoryList1(type,parentId)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    categoryListLiveData.postValue(response.body())
                }else{
                    onError(response.message())
                }
            }
        }

        return categoryListLiveData
    }


    fun getPartnerList(pcId:String): MutableLiveData<PartnerList> {
        val partnerListLiveData=MutableLiveData<PartnerList>()
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=repository.getPartnerList(pcId)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    partnerListLiveData.postValue(response.body())
                }else{
                    onError(response.message())
                }
            }
        }

        return partnerListLiveData
    }
}