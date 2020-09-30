package com.walker.fluencytest.data.network.listener

interface APIListener<T> {
    fun onSuccess(model: T)
    fun onError(str: String)
    fun onFailure(model: T)
}