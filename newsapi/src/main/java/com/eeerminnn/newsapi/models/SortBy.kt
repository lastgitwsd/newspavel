package com.eeerminnn.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortBy {

    @SerialName("relevancy")
    RELEVANCY,

    @SerialName("popularity")
    POPULARITI,

    @SerialName("publishedAt")
    PUBLISHED_AT,
}


