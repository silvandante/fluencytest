package com.walker.fluencytest.data.network.repositories


import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{
        private lateinit var retrofit: Retrofit
        private val baseurl = "https://dev.hackit.app/webapi/"

        private var tokenKey: String = ""

        private fun getRetrofitInstance() : Retrofit{

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor ( object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request =
                        chain.request()
                            .newBuilder()
                            .addHeader("x-hackit-auth", tokenKey)
                            .build()
                    return chain.proceed(request)
                }
            })

            if(!Companion::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseurl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun addHeader(token:String){
            Log.d("CALLED_ADD_TOKEN",token);
            tokenKey = token
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }

    }
}