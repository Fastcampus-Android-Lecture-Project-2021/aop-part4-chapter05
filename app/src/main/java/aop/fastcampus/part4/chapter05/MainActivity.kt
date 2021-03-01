package aop.fastcampus.part4.chapter05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import aop.fastcampus.part4.chapter05.data.database.DataBaseProvider
import aop.fastcampus.part4.chapter05.data.entity.GithubOwner
import aop.fastcampus.part4.chapter05.data.entity.GithubRepoEntity
import aop.fastcampus.part4.chapter05.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        GlobalScope.launch {
            val dataJob = addMockData()
            dataJob.join()
            val searchHistories = loadSearchHistory()
            withContext(Dispatchers.Main) {
                Log.e("histories", searchHistories.toString())
            }
        }
    }

    private fun initViews() = with(binding) {
        searchButton.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, SearchActivity::class.java)
            )
        }
    }

    private suspend fun addMockData() = GlobalScope.launch {
        withContext(Dispatchers.IO) {
            val mockData = (0..10).map {
                GithubRepoEntity(
                    name = "repo $it",
                    fullName = "name $it",
                    owner = GithubOwner(
                        "login",
                        "avatarUrl"
                    ),
                    description = null,
                    language = null,
                    updatedAt = Date().toString(),
                    stargazersCount = it
                )
            }
            DataBaseProvider.provideDB(this@MainActivity).searchHistoryDao().insertAll(mockData)
        }
    }

    private suspend fun loadSearchHistory(): List<GithubRepoEntity> =
        withContext(Dispatchers.IO) {
            val histories = DataBaseProvider.provideDB(this@MainActivity).searchHistoryDao().getHistory()
            histories
        }

}
