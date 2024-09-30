import com.icure.keberus.genPowJs
import com.icure.kryptom.utils.toHexString
import io.kotest.core.spec.style.StringSpec
import kotlin.random.Random

class PowTestJs: StringSpec({

    "Simple PoW test" {
        println(genPowJs(Random.nextBytes(16).toHexString(), "ironmansucks".encodeToByteArray(), 5000))
    }
})