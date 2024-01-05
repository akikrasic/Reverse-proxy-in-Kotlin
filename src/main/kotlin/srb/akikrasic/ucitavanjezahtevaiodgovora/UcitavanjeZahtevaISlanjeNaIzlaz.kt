package srb.akikrasic.ucitavanjezahtevaiodgovora

import java.io.InputStream
import java.io.OutputStream

class UcitavanjeZahtevaISlanjeNaIzlaz(override val inp: InputStream, override val out: OutputStream) : UcitavanjeISlanjeNaIzlaz(inp, out){
    val zahtev = Zahtev()

    override fun vratiteObjekat(): ZajednickoZaZahtevIOdgovor  = zahtev

    fun vratiteZahtev() = zahtev


    override fun dodeliteTriVrednostiUPrvomRedu(triVrednosti: TriVrednosti) {
        val (v1,v2,v3)=triVrednosti
        zahtev.metoda =v1
        zahtev.url = v2
        zahtev.protokolVerzija = v3

    }

    override fun ucitavanjeTela() {
        ucitavanjeNormalnogTela()
    }

}