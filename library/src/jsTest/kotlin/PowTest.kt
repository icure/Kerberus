import com.icure.keberus.ChallengeJs
import com.icure.keberus.resolveChallengeJs
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlin.random.Random

class PowTestJs: StringSpec({

    "Simple PoW test" {
        println(
            resolveChallengeJs(
                ChallengeJs(
                    Random.nextBytes(16).toHexString(),
                    arrayOf(Random.nextBytes(16).toHexString()),
                    5000
                ),
                "JRTFM"
            )
        )
    }
})