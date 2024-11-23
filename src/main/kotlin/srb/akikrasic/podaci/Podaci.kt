package srb.akikrasic.podaci

import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.ucitavanjeWebSocketa.WebSoketPoruka
import java.util.*

object Podaci {
    val listaSvihHttpZahtevaIOdgovora = Collections.synchronizedList(mutableListOf<KomunikacijaPodaci>())

    val listaSvihUrlovaZaWebSoket = Collections.synchronizedList(mutableListOf<String>())
    val mapaSvihUrlovaZaWebSoket  = Collections.synchronizedMap(mutableMapOf<String, UrlSaWebSoketPorukama>())
}