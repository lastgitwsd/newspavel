package com.eeerminnn.newsapi.models

import com.eeerminnn.newsapi.utils.DateTimeUTCSerializer
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class ArticleDTO(
    val source: SourceDTO,                   // ← объект, а не список
    val author: String? = null,              // многие поля могут быть null
    val title: String,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null,
    @Serializable(with = DateTimeUTCSerializer::class)
    val publishedAt: Date,
    val content: String? = null
)

@Serializable
data class SourceDTO(
    val id: String? = null,
    val name: String
)
