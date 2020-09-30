package com.walker.fluencytest.ui.auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.models.auth.AuthData
import com.walker.fluencytest.data.models.auth.AuthModel
import com.walker.fluencytest.data.models.user.UserModel
import com.walker.fluencytest.data.network.repositories.RetrofitClient
import com.walker.fluencytest.data.local.SecurityPreferences
import com.walker.fluencytest.data.network.repositories.AuthRepository




class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val mSharedPreferences = SecurityPreferences(application)

    private val mToken = MutableLiveData<String>()
    val token: LiveData<String> = mToken

    private val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = mLoggedUser

    private val mUserId = MutableLiveData<String>()
    val userId: LiveData<String> = mUserId


    /**
     * Faz login usando API
     */
    fun doLogin(authData: AuthData) {
        authRepository.login(authData, object : APIListener<AuthModel> {
            override fun onSuccess(model: AuthModel) {
                mSharedPreferences.store(
                    AppConstants.SHARED_PREFERENCES.TOKEN_KEY,
                    model.data.token
                )

                RetrofitClient.addHeader(model.data.token)

                mToken.value = model.data.token
            }

            override fun onError(str: String) {
                mToken.value = null
            }

            override fun onFailure(model: AuthModel) {
                mToken.value = model.meta.code.toString()
            }

        })
    }

    /**
    * Pega usuário correspondente ao token
     */

    fun getUser(tokenUser: String) {
        authRepository.getUser(tokenUser,object :APIListener<UserModel>{
            override fun onSuccess(model: UserModel) {
                mSharedPreferences.store(AppConstants.SHARED_PREFERENCES.USER_ID, model.data.user.id.toString())
                mSharedPreferences.store(AppConstants.SHARED_PREFERENCES.USER_NAME, model.data.user.firstName)

                mUserId.value = model.data.user.id.toString()
            }

            override fun onError(str: String) {
                mUserId.value = null
            }

            override fun onFailure(model: UserModel) {
                mUserId.value = null
            }

        })
    }

}