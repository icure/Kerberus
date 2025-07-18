
import com.icure.kerberus.Challenge
import com.icure.kerberus.Solution
import com.icure.kerberus.resolveChallenge
import com.icure.kerberus.validateSolution
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.test.assertTrue

class PowTest: StringSpec({

    "Simple PoW test" {
        val challenge = Challenge(
            Random.nextBytes(16).toHexString(),
            List(5) {
                Random.nextBytes(16).toHexString()
            },
            5000
        )

        val input = "JRTFM"

        val solution = resolveChallenge(
            challenge,
            input
        ) {
            println("Progress: ${it * 100}%")
        }

        assertTrue(validateSolution(challenge, solution, input))
    }

    "Validate dart" {
        val challenge = Challenge(
            Random.nextBytes(16).toHexString(),
            listOf(
                "9d876973-ebbe-44db-a33b-53944a7e0104",
                "9ea876a8-0300-497a-9876-69df00981f80",
                "c4284a65-89fa-40fd-bd03-1cf1d24f6bd0",
                "1a97960f-9dbe-4acc-bdf6-370bad8f3999",
                "e5a39e10-3f96-46cd-ba44-84629fbcf111",
                "bc5a1758-3164-4ed5-9da1-f9b1a8dd1d34",
                "b88a8fc0-75e9-4370-b0a2-902fb878a06c",
                "bf057ec2-5653-41eb-8b7c-5b7b42cdbc92",
                "ef973dfc-81f3-49d8-8594-edb89251d586",
                "ee61e4e8-989b-4f11-a8b0-fadd5b3d5d8a",
            ),
            50000
        )

        val input = "JRTFM"

        val solution = Solution(
            challenge.id,
            listOf(
                "59178",
                "126246",
                "17245",
                "37861",
                "113007",
                "37189",
                "17777",
                "22940",
                "147380",
                "1033"
            )
        )

        assertTrue(validateSolution(challenge, solution, input))
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