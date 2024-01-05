package srb.akikrasic.dekodiranje

import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

class DeflateDekoder: Dekoder {
    override fun dekodujte(ulaz: ByteArray): ByteArray {
        val dek = Inflater()
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        dek.setInput(ulaz)
        while(!dek.finished()){
            val brojUpisanih  = dek.inflate(buffer)
            bos.write(buffer, 0 , brojUpisanih)

        }
        dek.end()
        return bos.toByteArray()
    }

}