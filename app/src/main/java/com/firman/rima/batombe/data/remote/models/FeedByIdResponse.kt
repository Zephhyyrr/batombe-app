package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class FeedByIdResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: FeedDetail? = null
) {
    data class FeedDetail(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("fileAudio")
        val fileAudio: String? = null,

        @SerializedName("pantunBatombe")
        val pantunBatombe: String? = null,

        @SerializedName("like")
        val like: Int? = null,

        @SerializedName("userId")
        val userId: Int? = null,

        @SerializedName("isPublic")
        val isPublic: Boolean? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null,

        @SerializedName("user")
        val user: User? = null,

        @SerializedName("comments")
        val comments: List<Comment>? = null,

        @SerializedName("_count")
        val count: Count? = null,

        @SerializedName("isLiked")
        val isLiked: Boolean? = false

    )

    data class User(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("profileImage")
        val profileImage: String? = null
    )

    data class Count(
        @SerializedName("comments")
        val comments: Int? = null
    )

    data class Comment(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("content")
        val content: String? = null,

        @SerializedName("user")
        val user: User? = null
    )
}