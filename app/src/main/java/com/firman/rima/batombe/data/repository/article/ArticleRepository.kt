package com.firman.rima.batombe.data.repository.article

import com.firman.rima.batombe.data.remote.models.ArticleResponse
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getAllArticle(): Flow<ResultState<List<ArticleResponse.Data>>>
    suspend fun getArticleById(id: Int): Flow<ResultState<ArticleResponse.Data>>
}