import com.icure.kerberus.ChallengeJs
import com.icure.kerberus.resolveChallengeJs
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