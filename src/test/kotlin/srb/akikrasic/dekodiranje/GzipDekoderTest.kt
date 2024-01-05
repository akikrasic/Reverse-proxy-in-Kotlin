package srb.akikrasic.dekodiranje

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPOutputStream

class GzipDekoderTest : DekoderEnkoderKlasaZaTest(){

    val gzipDekoder  = GzipDekoder()

    @Test
    fun probaEnkodingDekoding() {
        enkodavanjeDekodovanje(gzipDekoder)
    }

    override fun enkodujteString(s: String): ByteArray {
        val byteOutputStream = ByteArrayOutputStream()
        val gzipOutputStream = GZIPOutputStream(byteOutputStream)

        gzipOutputStream.write(s.toByteArray(StandardCharsets.ISO_8859_1))
        gzipOutputStream.close()

        //return  byteOutputStream.toString(StandardCharsets.ISO_8859_1)
          return byteOutputStream.toByteArray()
    }
}
