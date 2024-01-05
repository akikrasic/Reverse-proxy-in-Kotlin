package srb.akikrasic.ucitavanjezahtevaiodgovora

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiVrednosti

class HederiCuvanjeIPretragaTest{

    val hederiCuvanje = HederiCuvanjeIPretraga()

    @BeforeEach
    fun postavljanje(){
        hederiCuvanje.dodajteHederIVrednost(HederiNazivi.contentLength, "1234")
        hederiCuvanje.dodajteHederIVrednost(HederiNazivi.transferEncoding, HederiVrednosti.chunked)
    }

    @Test
    fun proveraPretrageNalaziIspravno(){
        assertEquals("",hederiCuvanje.pretraga("kompir"))
        assertEquals(HederiVrednosti.chunked, hederiCuvanje.pretraga(HederiNazivi.transferEncoding))
        assertEquals(1234, PretvaranjeStringaUBroj.pretvaranjeStringaUBroj(hederiCuvanje.pretraga(HederiNazivi.contentLength)))
    }




}