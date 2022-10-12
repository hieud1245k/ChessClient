package com.hieuminh.chessclient.providers

import com.hieuminh.chessclient.common.constants.UrlConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientProvider {
    private var appRetrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (appRetrofit == null) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            appRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(UrlConstants.LOCAL_API_URL)
                .client(okHttpClient)
                .build()
        }
        return appRetrofit!!
    }

    inline fun <reified T> getService(): T {
        return getRetrofit().create(T::class.java)
    }
}
