package srb.akikrasic.komunikacija

import kotlinx.coroutines.channels.Channel
import srb.akikrasic.ucitavanjezahtevaiodgovora.ZajednickoZaZahtevIOdgovor

object Komunikacija {

    val kanalZaKomunikaciju=Channel<KomunikacijaPodaci>(20);

}