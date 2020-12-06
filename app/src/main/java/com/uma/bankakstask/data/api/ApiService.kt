package com.uma.bankakstask.data.api

import com.uma.bankakstask.data.model.AddNewProductData
import com.uma.bankakstask.data.model.BankakasData
import retrofit2.http.*

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
interface ApiService {
    @GET("{id}")
    suspend fun getFirstData(@Path("id") id : String): BankakasData

}