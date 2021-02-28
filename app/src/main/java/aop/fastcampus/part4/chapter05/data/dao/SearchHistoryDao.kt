package aop.fastcampus.part4.chapter05.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aop.fastcampus.part4.chapter05.data.entity.GithubRepoEntity

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun insert(repo: GithubRepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repoList: List<GithubRepoEntity>)

    @Query("SELECT * FROM githubrepository")
    suspend fun getHistory(): List<GithubRepoEntity>

    @Query("DELETE FROM githubrepository")
    suspend fun clearAll()

}
