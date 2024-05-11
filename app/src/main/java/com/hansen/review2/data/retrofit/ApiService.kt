package com.hansen.review2.data.retrofit

import com.hansen.review2.data.response.DetailUserResponse
import com.hansen.review2.data.response.FollowersResponseItem
import com.hansen.review2.data.response.FollowingResponseItem
import com.hansen.review2.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") username: String
    ): Call<GithubResponse>
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>
    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username")
        username: String?
    ): Call<List<FollowingResponseItem>>
}