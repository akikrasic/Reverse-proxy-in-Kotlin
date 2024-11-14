package srb.akikrasic.dekodiranje

import java.nio.charset.StandardCharsets

interface Dekoder {
    fun dekodujte(ulaz:ByteArray):ByteArray
    fun dekodujteUString(ulaz:ByteArray):String = String(dekodujte(ulaz), StandardCharsets.UTF_8)


}