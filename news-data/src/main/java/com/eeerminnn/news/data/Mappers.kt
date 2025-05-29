package com.eeerminnn.news.data

import com.eeerminnn.database.news.models.ArticleDBO
import com.eeerminnn.news.data.model.Article
import com.eeerminnn.news.data.model.Source
import com.eeerminnn.newsapi.models.ArticleDTO


// Преобразование из DBO (сущность БД) в доменную модель
internal fun ArticleDBO.toArticle(): Article = Article(
    cacheId = this.uid,
    source = Source(
        id = this.source.id,
        name = this.source.name
    ),
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

// Преобразование из DTO (ответ API) в доменную модель
internal fun ArticleDTO.toArticle(): Article = Article(
    cacheId = 0L, // при создании из сети ID ещё нет
    source = Source(
        id = this.source.id.orEmpty(),
        name = this.source.name
    ),
    author = this.author.orEmpty(),
    title = this.title,
    description = this.description.orEmpty(),
    url = this.url,
    urlToImage = this.urlToImage.orEmpty(),
    publishedAt = this.publishedAt,
    content = this.content.orEmpty()
)

// Преобразование из DTO в DBO (для сохранения в БД)
internal fun ArticleDTO.toArticleDbo(): ArticleDBO = ArticleDBO(
    uid = 0L, // Room сгенерирует уникальный ключ
    source = com.eeerminnn.database.news.models.Source(
        id = this.source.id.orEmpty(),
        name = this.source.name
    ),
    author = this.author.orEmpty(),
    title = this.title,
    description = this.description.orEmpty(),
    url = this.url,
    urlToImage = this.urlToImage.orEmpty(),
    publishedAt = this.publishedAt,
    content = this.content.orEmpty()
)