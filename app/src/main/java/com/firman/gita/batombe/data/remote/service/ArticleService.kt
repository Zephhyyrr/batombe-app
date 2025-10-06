package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.models.ArticleDetailResponse
import com.firman.gita.batombe.data.remote.models.ArticleResponse
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ArticleService {

    @GET(ApiConstant.ARTICLE_LIST)
    suspend fun getAllArticle(
        @Header("Authorization") token: String
    ): ArticleResponse

    @GET(ApiConstant.ARTICLE_DETAIL)
    suspend fun getArticleById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ArticleDetailResponse
}
