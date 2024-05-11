package com.hansen.review2.data.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)
@Parcelize
@Entity(tableName = "user")
data class ItemsItem(

	@ColumnInfo(name = "login")
	@field:SerializedName("login")
	val login: String? = null,

	@ColumnInfo(name = "avatar_url")
	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int? = null,

): Parcelable
