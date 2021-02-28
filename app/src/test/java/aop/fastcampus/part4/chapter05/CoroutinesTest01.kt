package aop.fastcampus.part4.chapter05

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest01 {

    @Test
    fun test01() = runBlocking {
        val time = measureTimeMillis {
            val name = getFirstName()
            val lastName = getLastName()
            print("Hello, $name $lastName")
        }
        print("measure time : $time")
    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val name = async { getFirstName() }
            val lastName = async { getLastName() }
            print("Hello, ${name.await()} ${lastName.await()}")
        }
        print("measure time : $time")
    }

    suspend fun getFirstName(): String {
        delay(1000)
        return "이"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "기정"
    }



}
