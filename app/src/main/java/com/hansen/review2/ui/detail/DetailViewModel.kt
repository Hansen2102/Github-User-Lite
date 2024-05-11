package com.hansen.review2.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hansen.review2.data.response.DetailUserResponse
import com.hansen.review2.data.response.FollowersResponseItem
import com.hansen.review2.data.response.FollowingResponseItem
import com.hansen.review2.data.response.ItemsItem
import com.hansen.review2.data.retrofit.ApiConfig
import com.hansen.review2.data.database.DbModule
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val db: DbModule): ViewModel() {
    companion object{
        private const val TAG = "DetailViewModel"
    }
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _listFollower = MutableLiveData<List<FollowersResponseItem>?>()
    val listFollower: LiveData<List<FollowersResponseItem>?> = _listFollower

    private val _listFollowing = MutableLiveData<List<FollowingResponseItem>?>()
    val listFollowing: LiveData<List<FollowingResponseItem>?> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val resultSuccessFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavortie = false

    fun setFavoriteUser(item: ItemsItem){
        viewModelScope.launch {
            item.let {
                if(isFavortie){
                    db.userDao.delete(item)
                    resultDeleteFavorite.value =true
                }else{
                    db.userDao.insert(item)
                    resultSuccessFavorite.value =true
                }
            }
            isFavortie = !isFavortie
        }

    }

    fun findFavorite(Id:Int,listenFavorite: ()->Unit){
        viewModelScope.launch {
            val user = db.userDao.findById(Id)
            if(user!= null){
                listenFavorite()
            }
        }
    }    fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun findFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<FollowingResponseItem>> {

            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                _isLoading.value = false
                val followResponse = response.body()
                if (response.isSuccessful && followResponse != null) {
                    _listFollowing.value = followResponse
                }

            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun findFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {

            override fun onResponse(
                call: Call<List<FollowersResponseItem>>,
                response: Response<List<FollowersResponseItem>>
            ) {
                _isLoading.value = false
                val followerResponse = response.body()
                if (response.isSuccessful && followerResponse != null) {
                    _listFollower.value = followerResponse
                }

            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}