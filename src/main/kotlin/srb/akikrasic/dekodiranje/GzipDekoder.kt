package srb.akikrasic.dekodiranje

import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

class GzipDekoder :Dekoder{
    override fun dekodujte(ulaz: ByteArray): ByteArray {
        try {
            val dekodovan = GZIPInputStream(ByteArrayInputStream(ulaz)).readAllBytes() ?: ByteArray(0)
            return dekodovan
        }catch(e:Exception){
            e.printStackTrace()
            println("gzip los format ${String(ulaz)}")
        }
        return ulaz
    }
}