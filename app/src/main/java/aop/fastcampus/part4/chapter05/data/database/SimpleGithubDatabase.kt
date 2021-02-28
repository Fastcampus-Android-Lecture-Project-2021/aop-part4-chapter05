package aop.fastcampus.part4.chapter05.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import aop.fastcampus.part4.chapter05.data.dao.SearchHistoryDao
import aop.fastcampus.part4.chapter05.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

}
