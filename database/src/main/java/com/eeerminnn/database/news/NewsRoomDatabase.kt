package com.eeerminnn.database.news

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eeerminnn.database.news.dao.ArticleDao
import com.eeerminnn.database.news.models.ArticleDBO
import com.eeerminnn.database.news.utils.Converters

/**
 * Обёртка над RoomDatabase, чтобы не выносить DAO наружу напрямую.
 */
class NewsDatabase internal constructor(
    private val database: NewsRoomDatabase
) {
    val articlesDao: ArticleDao
        get() = database.articlesDao()
}

@Database(
    entities = [ArticleDBO::class],
    version = 1
)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDao
}

fun NewsDatabase(applicationContext: Context): NewsDatabase {
    val newsRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDatabase::class.java,
        "news"
    )
        .build()
    return NewsDatabase(newsRoomDatabase)
}