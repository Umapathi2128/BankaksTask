package com.uma.bankakstask.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uma.bankakstask.data.repository.DataManager
import java.lang.IllegalArgumentException
/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
class MainActivityViewFactory(var dataManager: DataManager)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(dataManager) as T
        }
        throw IllegalArgumentException("Unkown class exception")
    }
}