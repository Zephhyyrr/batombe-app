package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class PostCommentResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: CommentData? = null
) {
    data class CommentData(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("content")
        val content: String? = null,

        @SerializedName("userId")
        val userId: Int? = null,

        @SerializedName("historyId")
        val historyId: Int? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null,

        @SerializedName("user")
        val user: User? = null
    )

    data class User(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("profileImage")
        val profileImage: String? = null
    )
}