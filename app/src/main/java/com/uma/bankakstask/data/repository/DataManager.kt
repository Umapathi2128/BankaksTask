package com.uma.bankakstask.data.repository

import android.content.Context
import com.uma.bankakstask.data.api.ApiHelper

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
class DataManager(
    val ctx: Context,
    val apiHelper: ApiHelper
) : DataHelper {
    override suspend fun getFirstData(id : String) = apiHelper.getFirstData(id)
}