package com.uma.bankakstask.utils

import com.uma.bankakstask.utils.Status.*

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2020-12-04.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object{
        fun <T> success(data : T) : Resource<T> = Resource(status = SUCCESS,data = data,message = null)

        fun <T> error(data : T,message: String?) : Resource<T> = Resource(status = ERROR,data=data,message = message)

        fun <T> loading(data : T) : Resource<T> = Resource(status = LOADING,data,message = null)
    }
}