package com.uma.bankakstask.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.uma.bankakstask.data.repository.DataManager
import com.uma.bankakstask.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
class MainActivityViewModel(val dataManager: DataManager) : ViewModel() {

    fun callFirstDataApi(id : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = dataManager.getFirstData(id)
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Exception occured"))
        }
    }


}