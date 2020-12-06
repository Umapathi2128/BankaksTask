package com.uma.bankakstask.data.api

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
class ApiHelper(private val apiService: ApiService) {

    suspend fun getFirstData(id : String) = apiService.getFirstData(id)

}