package ru.uomkri.tchktest.repo.net

import com.squareup.moshi.Json

data class BaseUserData(
        val id: Int,
        val login: String,
        @Json(name = "avatar_url")
        val avatarUrl: String,
        val type: String
)

data class SearchResult(
        @Json(name = "total_count")
        val totalCount: Int,
        val items: List<BaseUserData>,
        val message: String?
)

data class User(
        val id: Int,
        val login: String,
        @Json(name = "avatar_url")
        val avatarUrl: String,
        val company: String?,
        val type: String?,
        val name: String?,
        val message: String?
)

data class VKAccount(
        @Json(name = "first_name")
        val firstName: String,
        @Json(name = "last_name")
        val lastName: String,
        @Json(name = "screen_name")
        val screenName: String,
        val id: Int,
        @Json(name = "photo_100")
        val profilePicture: String?
)