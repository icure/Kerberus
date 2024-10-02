import com.icure.keberus.ConfigJs
import com.icure.keberus.genPowJs
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlin.random.Random

class PowTestJs: StringSpec({

    "Simple PoW test" {
        println(
            genPowJs(
                ConfigJs(
                    Random.nextBytes(16).toHexString(),
                    arrayOf(Random.nextBytes(16).toHexString()),
                    5000
                ),
                "JRTFM"
            )
        )
    }
})