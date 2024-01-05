package srb.akikrasic.dekodiranje

interface Dekoder {
    fun dekodujte(ulaz:ByteArray):ByteArray
    fun dekodujteUString(ulaz:ByteArray):String = String(dekodujte(ulaz))


}