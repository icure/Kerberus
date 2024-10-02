import com.icure.keberus.genPow
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PowTest: StringSpec({

    "Simple PoW test" {
        genPow(Random.nextBytes(16).toHexString(), "ironmansucks".encodeToByteArray()).let {
            println(it)
        }
    }

    "!benchmark" {
        withContext(Dispatchers.Default) {
            List(10000) { index ->
                async {
                    println("Started $index")
                    genPow(Random.nextBytes(16).toHexString(), "ironmansucks".encodeToByteArray()).also {
                        println("Done $index")
                    }
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 5000")
                println("average nounces: ${it.map { it.nonce.toLong() }.average()}")
                println("median nounces : ${it.map { it.nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.map { it.nonce.toLong() }.maxOrNull()}")
                println("min nounces: ${it.map { it.nonce.toLong() }.minOrNull()}")
            }

            List(10000) {
                async {
                    genPow(Random.nextBytes(16).toHexString(), "ironmansucks".encodeToByteArray())
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 10000")
                println("average nounces: ${it.map { it.nonce.toLong() }.average()}")
                println("median nounces : ${it.map { it.nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.map { it.nonce.toLong() }.maxOrNull()}")
                println("min nounces: ${it.map { it.nonce.toLong() }.minOrNull()}")
            }

            List(10000) {
                async {
                    genPow(Random.nextBytes(16).toHexString(), "ironmansucks".encodeToByteArray())
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 20000")
                println("average nounces: ${it.map { it.nonce.toLong() }.average()}")
                println("median nounces : ${it.map { it.nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.map { it.nonce.toLong() }.maxOrNull()}")
                println("min nounces: ${it.map { it.nonce.toLong() }.minOrNull()}")
            }
        }
    }
})