package srb.akikrasic.ucitavanjezahtevaiodgovora

class UcitaniIntIBajt(val ucitaniInt :Int){
    val ucitaniBajt:Byte
    val ucitaniKarakter:Char
    init{
        ucitaniBajt = ucitaniInt.toByte()
        ucitaniKarakter = ucitaniInt.toChar()
    }
}
val ucitaniIntIBajtMinus1 = UcitaniIntIBajt(-1 )
