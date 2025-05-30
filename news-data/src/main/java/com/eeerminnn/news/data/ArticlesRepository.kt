package com.eeerminnn.news.data

import com.eeerminnn.common.Logger
import com.eeerminnn.database.news.NewsDatabase
import com.eeerminnn.database.news.models.ArticleDBO
import com.eeerminnn.news.data.model.Article
import com.eeerminnn.newsapi.NewsApi
import com.eeerminnn.newsapi.models.ArticleDTO
import com.eeerminnn.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach


class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val logger: Logger,
) {

    fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllAArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer(query)

        return cachedAllAArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapConcat { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }


    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything(query = "android")) }
            .onEach { result ->
                if (result.isSuccess) saveNetResponseToCache(result.getOrThrow().articles)
            }
            .onEach { result ->
                if (result.isFailure) {
                    logger.e(
                        LOG_TAG,
                        "Error geting data from server. Cause = ${result.exceptionOrNull()}"
                    )
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start)
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }


    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest = database.articlesDao::getAll.asFlow()
            .map { RequestResult.Success(it) }
            .catch {
                RequestResult.Error<List<ArticleDBO>>(error = it)
                logger.e(LOG_TAG, "Error geting from database. Cause = $it")
            }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest).map { result ->
            result.map { articlesDbos ->
                articlesDbos.map { it.toArticle() }
            }
        }
    }

//    suspend fun search(query: String): Flow<Article> {
//        api.everything()
//        TODO("Not implemented")
//    }
//
//    fun fetchLatest(): Flow<RequestResult<List<Article>>> {
//        return getAllFromServer()
//
//    }


    private companion object {
        const val LOG_TAG = "ArticlesRepository"
    }
}





