package srb.akikrasic.dekodiranje

import com.nixxcode.jvmbrotli.common.BrotliLoader
import com.nixxcode.jvmbrotli.dec.Decoder

class BrotliDekoder : Dekoder {

    init{
        BrotliLoader.isBrotliAvailable()
    }
    override fun dekodujte(ulaz: ByteArray): ByteArray {
        try{
        val dekodiran  = Decoder.decompress(ulaz)?:ByteArray(0)
        return dekodiran
        }
        catch(e:Exception){
            e.printStackTrace()
            println("Brotli los format ${String(ulaz)}")
        }
        return ulaz
    }
}