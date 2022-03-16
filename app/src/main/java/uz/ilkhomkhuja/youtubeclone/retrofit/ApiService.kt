package uz.ilkhomkhuja.youtubeclone.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.ilkhomkhuja.youtubeclone.models.MyYouTube

interface ApiService {
    @GET("search")
    suspend fun getVideoByTag(
        @Query("part") part: String = "snippet,id",
        @Query("q") q: String,
        @Query("key") key: String = "YOUR_KEY_HERE",
        @Query("maxResults") maxResults: Int = 5
    ): Response<MyYouTube>
}


