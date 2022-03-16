package uz.ilkhomkhuja.youtubeclone.repository

import uz.ilkhomkhuja.youtubeclone.retrofit.ApiService

class YoutubeRepository(private var apiService: ApiService) {

    suspend fun getVideoByTag(q: String) =
        apiService.getVideoByTag("snippet", q, "AIzaSyCSasUx_n_PEjEmm9G0xu0UZS9Cqd-WhIY")

}