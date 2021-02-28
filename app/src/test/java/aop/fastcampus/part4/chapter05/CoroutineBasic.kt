package aop.fastcampus.part4.chapter05

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis


class CoroutineBasic {

    /**
     * [Coroutine] 이란?
     * Suspendable Computation - 일시정지 가능한 연산
     * 코루틴 생성 => 쓰레드 상관없이 정지, 재생, 쓰레드에 바인딩되지 않습니다.
     *
     * [suspend function] 이란?
     *
     * 1. suspend keyword 붙인것
     * 2. suspending lambda
     *
     * async => await() sequence => yield()
     *
     * 코루틴 빌더
     * launch, sequence, async
     *
     * 쓰레드를 차단하는 것이 아니라, 코드 실행을 일시중단한 것
     *
     *
     *
     */

    @Test
    fun basic01() = runBlocking {
        println("${Thread.activeCount()} thread active count - start")
        val time = measureTimeMillis {
            createCoroutines(3)
        }
        println("${Thread.activeCount()} thread active count end")
        println("took time : $time")
    }

    suspend fun createCoroutines(amount: Int) {
        val jobs = mutableListOf<Job>()
        (0 until amount).forEach {
            jobs += GlobalScope.launch {
                println("Start Coroutine index: $it thread : ${Thread.currentThread()}")
                delay(1000)
                println("Finish Coroutine index: $it thread : ${Thread.currentThread()}")
            }
        }
        jobs.joinAll()
    }

}
