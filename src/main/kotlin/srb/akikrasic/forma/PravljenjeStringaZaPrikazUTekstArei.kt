package srb.akikrasic.forma

import srb.akikrasic.komunikacija.KomunikacijaPodaci

class PravljenjeStringaZaPrikazUTekstArei {

    fun napraviteTekstOdKomunikacije(k:KomunikacijaPodaci):String{
        val sb = StringBuilder()
        k.hederi.mapaOriginalnihHedera.forEach { kljuc, v-> sb.append(kljuc).append(": ").append(v).append("\n") }
        sb.append("\n").append(k.poruka)
        return sb.toString()
    }
}