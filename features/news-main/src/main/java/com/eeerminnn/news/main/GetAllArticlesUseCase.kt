package com.eeerminnn.news.main

import com.eeerminnn.news.data.ArticlesRepository
import com.eeerminnn.news.data.model.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator  fun  invoke(): Flow<Article> {
        return repository.getAll()
    }

}