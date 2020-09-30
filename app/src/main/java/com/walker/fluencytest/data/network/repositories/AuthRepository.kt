package com.walker.fluencytest.data.network.repositories

import android.util.Log
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.models.auth.AuthData
import com.walker.fluencytest.data.models.auth.AuthModel
import com.walker.fluencytest.data.models.user.UserModel
import com.walker.fluencytest.data.network.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {

    private val remote = RetrofitClient.createService(AuthService::class.java)

    fun login(authData: AuthData, listener: APIListener<AuthModel>){
        val call: Call<AuthModel> = remote.login(authData)
        call.enqueue(object: Callback<AuthModel>{
            override fun onResponse(call: Call<AuthModel>, response: Response<AuthModel>) {

                if (response.body()?.meta?.code==201)
                    response.body()?.let { listener.onSuccess(it) }
                else
                    response.body()?.let { listener.onFailure(it) }

            }

            override fun onFailure(call: Call<AuthModel>, t: Throwable) {
                Log.d("Auth_shit_error",t.message.toString())
                listener.onError((t.message.toString()))
            }

        })
    }


    fun getUser(token: String, listenerUser: APIListener<UserModel>){
        val call: Call<UserModel> = remote.getUser(token)
        call.enqueue(object: Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.body()?.meta?.code==200)
                    response.body()?.let { listenerUser.onSuccess(it) }
                else
                    response.body()?.let { listenerUser.onFailure(it) }

            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                listenerUser.onError((t.message.toString()))
            }

        })
    }
}