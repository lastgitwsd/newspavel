package com.eeerminnn.news.main

import com.eeerminnn.news.data.ArticlesRepository
import com.eeerminnn.news.data.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.eeerminnn.news.data.map
import com.eeerminnn.news.data.model.Article as DataArticle

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<Article>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles -> articles.map { it.toUiArticle() } }
            }
    }
}

private fun DataArticle.toUiArticle(): Article {
    TODO()
}

