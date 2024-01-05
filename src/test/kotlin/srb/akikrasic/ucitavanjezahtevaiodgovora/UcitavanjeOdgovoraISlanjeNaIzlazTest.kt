package srb.akikrasic.ucitavanjezahtevaiodgovora

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiVrednosti
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UcitavanjeOdgovoraISlanjeNaIzlazTest{
    val out = ByteArrayOutputStream()
    val verzija ="HTTP/1.1"
    val statusKod = "200"
    val poruka = "OK"
    val telo = "Proba 123"
    val duzina = telo.length

    val chunkedTekst = "Proba 123456"
    val chunkedTekstDodatak = "ab"
    val chunkedTelo = "c\r\n${chunkedTekst}"
    val chunkedTeloDrugiDeo = "2\r\n${chunkedTekstDodatak}"
    val chunkedTeloKraj = "0\r\n"
    val odgovor ="${verzija} ${statusKod} ${poruka}\r\n${HederiNazivi.contentLength}: ${duzina}\r\n${HederiNazivi.contentEncoding}: ${HederiVrednosti.br}\r\n\r\n${telo}"
    val odgovorChunked ="${verzija} ${statusKod} ${poruka}\r\n${HederiNazivi.contentLength}: ${duzina}\r\n${HederiNazivi.transferEncoding}: ${HederiVrednosti.chunked}\r\n${HederiNazivi.contentEncoding}: ${HederiVrednosti.br}\r\n\r\n${chunkedTelo}\r\n${chunkedTeloDrugiDeo}\r\n${chunkedTeloKraj}\r\n"


    fun napraviteObraduIOdgovor(odgovor:String)=
        UcitavanjeOdgovoraISlanjeNaIzlaz(
            ByteArrayInputStream(odgovor.toByteArray()),
            out
        )

    fun proveraDaLiSamoPrepisujeOdJedanNaDrugi(){


        val odgovorISlanjeNaIzlaz = napraviteObraduIOdgovor(odgovor)

        for (i in 0..odgovor.lastIndex){
            odgovorISlanjeNaIzlaz.samoUcitavanjeIPrepisivanje()
        }

        assertEquals(odgovor, out.toString())
    }

    fun napraviteOdgovorObjekat(odgovorTekst:String):CeoOdgovor{
        val odgovorISlanjeNaIzlaz = napraviteObraduIOdgovor(odgovorTekst)
        odgovorISlanjeNaIzlaz.ucitavanjeISlanjeNaIzlaz()
        return odgovorISlanjeNaIzlaz.vratiteOdgovor()
    }

    fun proveraOsnovnihStvari(ceoOdgovorObjekat:CeoOdgovor){
        assertEquals(verzija, ceoOdgovorObjekat.protokolVerzija)
        assertEquals(statusKod, ceoOdgovorObjekat.statusKod)
        assertEquals(poruka, ceoOdgovorObjekat.statusPoruka)
        assertEquals(HederiVrednosti.br, ceoOdgovorObjekat.hederi.pretraga(HederiNazivi.contentEncoding))
        assertEquals(telo, String(ceoOdgovorObjekat.telo))
    }
    @Test
    fun proveraDaLiLepoIzdvajaSveNormalno(){
        val odgovorObjekat = napraviteOdgovorObjekat(odgovor)
        proveraOsnovnihStvari( odgovorObjekat )
        assertEquals(duzina, PretvaranjeStringaUBroj.pretvaranjeStringaUBroj(odgovorObjekat.hederi.pretraga(HederiNazivi.contentLength)))

    }

    @Test
    fun proveraDaLiLepoRadiSveNormalnoChunked(){
        val odgovorObjekat = napraviteOdgovorObjekat(odgovorChunked)
        assertEquals(HederiVrednosti.chunked, odgovorObjekat.hederi.pretraga(HederiNazivi.transferEncoding))
        assertEquals("${chunkedTekst}${chunkedTekstDodatak}", String(odgovorObjekat.telo))
    }

}