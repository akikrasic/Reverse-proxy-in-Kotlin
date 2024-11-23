package srb.akikrasic.podaci

import srb.akikrasic.ucitavanjeWebSocketa.WebSoketPoruka
import java.util.*

class UrlSaWebSoketPorukama(val url:String) {
    val listaPoruka  = Collections.synchronizedList(mutableListOf<WebSoketPoruka>())
}