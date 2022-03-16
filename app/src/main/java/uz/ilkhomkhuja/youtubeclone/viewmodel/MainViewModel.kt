package uz.ilkhomkhuja.youtubeclone.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import uz.ilkhomkhuja.youtubeclone.models.MyYouTube
import uz.ilkhomkhuja.youtubeclone.repository.YoutubeRepository
import uz.ilkhomkhuja.youtubeclone.retrofit.ApiClient
import uz.ilkhomkhuja.youtubeclone.utils.NetworkHelper
import uz.ilkhomkhuja.youtubeclone.utils.Resource

class MainViewModel : ViewModel() {

    private var searchRepository = YoutubeRepository(ApiClient.apiService)
    private var liveData = MutableLiveData<Resource<MyYouTube>>()

    fun getVideoByTag(context: Context, q: String): LiveData<Resource<MyYouTube>> {
        viewModelScope.launch {
            if (NetworkHelper(context).isNetworkConnected()) {
                coroutineScope {
                    supervisorScope {
                        try {
                            liveData.postValue(Resource.loading(null))
                            val video = searchRepository.getVideoByTag(q)
                            if (video.isSuccessful) {
                                liveData.postValue(Resource.success(video.body()))
                            } else {
                                liveData.postValue(
                                    Resource.error(
                                        video.raw().toString(),
                                        null
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            liveData.postValue(Resource.error(e.message ?: "Error", null))
                        }
                    }
                }
            } else {
                liveData.postValue(
                    Resource.error(
                        "Internet no connection !!",
                        null
                    )
                )
            }
        }
        return liveData
    }
}

