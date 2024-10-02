import com.icure.keberus.Challenge
import com.icure.keberus.resolveChallenge
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PowTest: StringSpec({

    "Simple PoW test" {
        resolveChallenge(
            Challenge(
                Random.nextBytes(16).toHexString(),
                listOf(Random.nextBytes(16).toHexString()),
                5000
            ),
            "JRTFM"
        ).let {
            println(it)
        }
    }

    "!benchmark" {
        withContext(Dispatchers.Default) {
            List(10000) { index ->
                async {
                    println("Started $index")
                    resolveChallenge(
                        Challenge(
                            Random.nextBytes(16).toHexString(),
                            listOf(Random.nextBytes(16).toHexString()),
                            5000
                        ),
                        "JRTFM"
                    ).also {
                        println("Done $index")
                    }
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 5000")
                println("average nounces: ${it.first().nonces.map { nonce -> nonce.toLong() }.average()}")
                println("median nounces : ${it.first().nonces.map { nonce -> nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.first().nonces.maxOfOrNull { nonce -> nonce.toLong() }}")
                println("min nounces: ${it.first().nonces.minOfOrNull { nonce -> nonce.toLong() }}")
            }

            List(10000) {
                async {
                    resolveChallenge(
            Challenge(
                Random.nextBytes(16).toHexString(),
                listOf(Random.nextBytes(16).toHexString()),
                5000
            ),
            "JRTFM"
        )
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 10000")
                println("average nounces: ${it.first().nonces.map { nonce -> nonce.toLong() }.average()}")
                println("median nounces : ${it.first().nonces.map { nonce -> nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.first().nonces.maxOfOrNull { nonce -> nonce.toLong() }}")
                println("min nounces: ${it.first().nonces.minOfOrNull { nonce -> nonce.toLong() }}")
            }

            List(10000) {
                async {
                    resolveChallenge(
            Challenge(
                Random.nextBytes(16).toHexString(),
                listOf(Random.nextBytes(16).toHexString()),
                5000
            ),
            "JRTFM"
        )
                }
            }.awaitAll().let {
                println("Generated 10000 PoW with difficulty 20000")
                println("average nounces: ${it.first().nonces.map { nonce -> nonce.toLong() }.average()}")
                println("median nounces : ${it.first().nonces.map { nonce -> nonce.toLong() }.sorted()[5000]}")

                println("max nounces: ${it.first().nonces.maxOfOrNull { nonce -> nonce.toLong() }}")
                println("min nounces: ${it.first().nonces.minOfOrNull { nonce -> nonce.toLong() }}")
            }
        }
    }
})