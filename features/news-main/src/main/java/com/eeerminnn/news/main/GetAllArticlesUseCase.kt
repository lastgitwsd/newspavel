package com.eeerminnn.news.main

import com.eeerminnn.news.data.ArticlesRepository
import com.eeerminnn.news.data.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.eeerminnn.news.data.map
import jakarta.inject.Inject
import com.eeerminnn.news.data.model.Article as DataArticle

class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {

    internal operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query)
            .map { requestResult ->
                requestResult.map { articles -> articles.map { it.toUiArticle() } }
            }
    }
}

private fun DataArticle.toUiArticle(): ArticleUI {
    return ArticleUI(
        id = cacheId,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url,
    )
}

