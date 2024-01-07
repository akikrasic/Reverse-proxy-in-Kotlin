package srb.akikrasic.komunikacija

import srb.akikrasic.odgovor.Odgovor
import srb.akikrasic.ucitavanjezahtevaiodgovora.CeoOdgovor
import srb.akikrasic.ucitavanjezahtevaiodgovora.HederiCuvanjeIPretraga
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev
import srb.akikrasic.ucitavanjezahtevaiodgovora.ZajednickoZaZahtevIOdgovor
import java.net.Socket

data class KomunikacijaPodaci(val host:String, val zahtev:Zahtev, val odgovor: CeoOdgovor) {
}