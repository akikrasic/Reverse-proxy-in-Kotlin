package srb.akikrasic.dekodiranje

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater

class DeflateDekoderTest:DekoderEnkoderKlasaZaTest(){

    val dekoder = DeflateDekoder()
    @Test
    fun dekodovanje(){
        enkodavanjeDekodovanje(dekoder)
    }

    override fun enkodujteString(s: String): ByteArray {
        val enk =  Deflater()
        enk.setInput(s.toByteArray())
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        enk.finish()
        while(!enk.finished()){

            val brojUpisanih  = enk.deflate(buffer)

            println(brojUpisanih)


            bos.write(buffer, 0 , brojUpisanih)
            bos.flush()

        }
        return bos.toByteArray()
    }
}
