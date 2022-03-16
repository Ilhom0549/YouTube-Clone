package uz.ilkhomkhuja.youtubeclone.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    var apiService: ApiService = getRetrofit().create(ApiService::class.java)
}