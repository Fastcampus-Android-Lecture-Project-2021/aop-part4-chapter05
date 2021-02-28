package aop.fastcampus.part4.chapter05

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

data class Profile(
    val userInfo: UserInfo,
    val contactInfo: ContactInfo
)

data class UserInfo(
    val name: String,
    val age: Int
)

data class ContactInfo(
    val phoneNumber: String,
    val address: String
)

class Palleral {

    @Test
    fun palleral() {
        val time = measureTimeMillis {
            getProfile()
        }
        println("measure time : $time")
    }

    fun getProfile(): Profile {
        val userInfo = getUserInfo() // 1
        val contactInfo = getContactInfo() // 1
        return createProfile(userInfo, contactInfo) // 1
    }

    fun getUserInfo(): UserInfo {
        Thread.sleep(1000)
        return UserInfo(name = "이기정", age = 20)
    }

    fun getContactInfo(): ContactInfo {
        Thread.sleep(1000)
        return ContactInfo(phoneNumber = "010000000", address = "서울시")
    }

    fun createProfile(userInfo: UserInfo, contactInfo: ContactInfo): Profile {
        Thread.sleep(1000)
        return Profile(userInfo, contactInfo)
    }

}

class Concurrent {
    @Test
    fun concurrent() {
        val time = measureTimeMillis {
            runBlocking {
                getProfile()
            }
        }
        println("measure time : $time")
    }

    suspend fun getProfile(): Profile {
        val userInfo = getAsyncUserInfo()
        val contactInfo = getAsyncContactInfo()
        return asyncCreateProfile(userInfo.await(), contactInfo.await()) // 1
            .await() // 1
    }

    fun getAsyncUserInfo() = GlobalScope.async {
        Thread.sleep(1000)
        UserInfo(name = "이기정", age = 20)
    }

    fun getAsyncContactInfo() = GlobalScope.async {
        Thread.sleep(1000)
        ContactInfo(phoneNumber = "010000000", address = "서울시")
    }

    fun asyncCreateProfile(userInfo: UserInfo, contactInfo: ContactInfo) = GlobalScope.async {
        Thread.sleep(1000)
        Profile(userInfo, contactInfo)
    }
}
