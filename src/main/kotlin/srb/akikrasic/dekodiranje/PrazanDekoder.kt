package srb.akikrasic.dekodiranje

class PrazanDekoder : Dekoder {
    override fun dekodujte(ulaz: ByteArray): ByteArray = ulaz
}