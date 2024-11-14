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

    suspend override fun ucitavanjeTela() {
        ucitavanjeNormalnogTela()
    }

    override fun toString()=
        """
            ${zahtev.metoda} ${zahtev.url} ${zahtev.protokolVerzija}
            ${zahtev.hederi.mapaOriginalnihHedera.map { "${it.key}: ${it.value}\n" }}
            ${zahtev.telo}
        """.trimIndent()

}