package srb.akikrasic.odgovor

data class Odgovor( val chunked : Boolean, val verzija:String, val status:String, val poruka:String, val headeri:Map<String, String>, val telo:ByteArray ) {

    fun poruka():String = String(telo)
}
fun chunkedOdgovor(telo:ByteArray):Odgovor = Odgovor(true, "", "", "", mapOf<String, String>(), telo )