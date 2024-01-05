package srb.akikrasic.komunikacija

import srb.akikrasic.ucitavanjezahtevaiodgovora.HederiCuvanjeIPretraga
import srb.akikrasic.ucitavanjezahtevaiodgovora.ZajednickoZaZahtevIOdgovor
import java.net.Socket

data class KomunikacijaPodaci(val host:String, val url:String, val metoda:String, val hederi:HederiCuvanjeIPretraga,val poruka:String) {
}