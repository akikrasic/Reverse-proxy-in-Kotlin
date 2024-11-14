package srb.akikrasic.dekodiranje

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

class DeflateDekoder: Dekoder {
    override fun dekodujte(ulaz: ByteArray): ByteArray {
        val dek = Inflater(true)
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(32768)
        dek.setInput(ulaz)
        while(!dek.finished()){
            val brojUpisanih  = dek.inflate(buffer)
            bos.write(buffer, 0 , brojUpisanih)

        }
        dek.end()
        return bos.toByteArray()
//        val inputStream = InflaterInputStream(ByteArrayInputStream(ulaz))
//        val bos = ByteArrayOutputStream()
//        val bafer = ByteArray(33792)
//        while( inputStream.available()>0){
//            val i = inputStream.read(bafer)
//            bos.write(bafer, 0, i+1)
//        }
//        return bos.toByteArray()

    }

}