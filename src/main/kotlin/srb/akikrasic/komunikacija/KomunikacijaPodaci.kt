package srb.akikrasic.komunikacija

import srb.akikrasic.ucitavanjezahtevaiodgovora.CeoOdgovor
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev

data class KomunikacijaPodaci(val host:String, val zahtev:Zahtev, val odgovor: CeoOdgovor) {
}