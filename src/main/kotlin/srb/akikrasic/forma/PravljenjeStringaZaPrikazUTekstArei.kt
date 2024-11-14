package srb.akikrasic.forma

import srb.akikrasic.dekodiranje.RadSaDekoderima
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.ucitavanjezahtevaiodgovora.ZajednickoZaZahtevIOdgovor
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi

class PravljenjeStringaZaPrikazUTekstArei {
    val radSaDekoderima = RadSaDekoderima()
    private fun napraviteTekstOdZajednickogObjekta(zajednickoZaZahtevIOdgovor: ZajednickoZaZahtevIOdgovor):String{
        val sb = StringBuilder()
        sb.append(zajednickoZaZahtevIOdgovor.protokolVerzija).append("\n")
        val dekoder = radSaDekoderima.vratiteDekoder(zajednickoZaZahtevIOdgovor.hederi.pretraga(HederiNazivi.contentEncoding))
        zajednickoZaZahtevIOdgovor.hederi.mapaOriginalnihHedera.forEach { kljuc, v-> sb.append(kljuc).append(": ").append(v).append("\n") }
        sb.append("\n").append(String(dekoder.dekodujte(zajednickoZaZahtevIOdgovor.telo)))
        return sb.toString()
    }

    fun napraviteTekstOdOdgovora(k:KomunikacijaPodaci):String = napraviteTekstOdZajednickogObjekta(k.odgovor)

    fun napraviteTekstOdZahteva(k: KomunikacijaPodaci):String = napraviteTekstOdZajednickogObjekta(k.zahtev)
}