package com.firman.gita.batombe.data.repository.article

import com.firman.gita.batombe.data.remote.models.ArticleResponse
import com.firman.gita.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getAllArticle(): Flow<ResultState<List<ArticleResponse.Data>>>
    suspend fun getArticleById(id: Int): Flow<ResultState<ArticleResponse.Data>>
}