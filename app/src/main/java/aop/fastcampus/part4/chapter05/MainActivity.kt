package aop.fastcampus.part4.chapter05

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import aop.fastcampus.part4.chapter05.databinding.ActivityMainBinding
import aop.fastcampus.part4.chapter05.utillity.AuthTokenProvider
import aop.fastcampus.part4.chapter05.utillity.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityMainBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val authTokenProvider by lazy { AuthTokenProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            loginGithub()
        }
    }

    private fun loginGithub() {
        val loginUri = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(this, loginUri)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.getQueryParameter("code")?.let { code ->
            GlobalScope.launch {
                showProgress()
                val progressJob = getAccessToken(code)
                progressJob.join()
                dismissProgress()
            }
        }

    }

    private suspend fun showProgress() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            with(binding) {
                loginButton.isGone = true
                progressBar.isGone = false
                progressTextView.isGone = false
            }
        }
    }

    private fun getAccessToken(code: String) = launch(coroutineContext) {
        try {
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.authApiService.getAccessToken(
                    clientId = BuildConfig.GITHUB_CLIENT_ID,
                    clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
                    code = code
                )
                val accessToken = response.accessToken
                Log.e("accessToken", accessToken)
                if (accessToken.isNotEmpty()) {
                    withContext(coroutineContext) {
                        authTokenProvider.updateToken(accessToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@MainActivity, "로그인 과정에서 에러가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dismissProgress() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            with(binding) {
                loginButton.isGone = false
                progressBar.isGone = true
                progressTextView.isGone = true
            }
        }
    }

}
