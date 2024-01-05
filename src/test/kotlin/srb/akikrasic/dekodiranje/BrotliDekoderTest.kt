package srb.akikrasic.dekodiranje
import com.nixxcode.jvmbrotli.common.BrotliLoader
import com.nixxcode.jvmbrotli.enc.Encoder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

class BrotliDekoderTest:DekoderEnkoderKlasaZaTest(){

    val dekoder = BrotliDekoder()
    @Test
    fun dekodovanje(){
       this.enkodavanjeDekodovanje(dekoder)
    }

    override fun enkodujteString(s: String): ByteArray  = Encoder.compress(s.toByteArray())
}