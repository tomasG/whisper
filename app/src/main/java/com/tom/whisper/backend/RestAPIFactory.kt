package com.tom.whisper.backend

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RestAPIFactory {

    companion object {
        var BASE_URL: String = "http://prod.whisper.sh"
        var retrofit:Retrofit? = null

        fun generateRetrofitClient(): BackendAPI? {
            if(retrofit == null) {
                val logging = HttpLoggingInterceptor()
                val httpClient = OkHttpClient.Builder()
                logging.level = HttpLoggingInterceptor.Level.BODY

                httpClient.addInterceptor(logging)

              retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(httpClient.build())
                        .build()

            }
            return retrofit?.create(BackendAPI::class.java)
        }
    }
}