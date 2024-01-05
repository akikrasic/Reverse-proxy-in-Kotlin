package srb.akikrasic.ucitavanjezahtevaiodgovora

class HederiCuvanjeIPretraga {
    val mapaOriginalnihHedera = mutableMapOf<String, String>()
    val mapaHederaZaPretragu = mutableMapOf<String, String>()

    fun dodajteHederIVrednost(heder:String, vrednost:String){
        mapaOriginalnihHedera[heder]=vrednost
        mapaHederaZaPretragu[heder.uppercase()] =vrednost
    }

    fun pretraga(heder:String):String =
        mapaHederaZaPretragu[heder.uppercase()]?:""

}