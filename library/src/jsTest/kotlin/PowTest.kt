import com.icure.kerberus.ChallengeJs
import com.icure.kerberus.resolveChallengeJs
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlin.js.json
import kotlin.random.Random

class PowTestJs: StringSpec({

    "Simple PoW test" {
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        println(
            resolveChallengeJs(
                json(
                    "id" to Random.nextBytes(16).toHexString(),
                    "salt" to arrayOf(Random.nextBytes(16).toHexString()),
                    "difficultyFactor" to 5000
                ) as ChallengeJs,
                "JRTFM"
            )
        )
    }
})