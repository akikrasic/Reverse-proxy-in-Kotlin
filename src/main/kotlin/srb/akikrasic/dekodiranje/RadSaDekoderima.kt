package srb.akikrasic.dekodiranje

class RadSaDekoderima {
    val prazanDekoder = PrazanDekoder()
    private val mapaSaDekoderima = mapOf(
        "br" to BrotliDekoder(),
        "gzip" to GzipDekoder(),
        "deflate" to DeflateDekoder()
    )

    fun vratiteDekoder(kljuc:String):Dekoder = mapaSaDekoderima.getOrDefault(kljuc, prazanDekoder)
    fun prazan() = prazanDekoder
}