package srb.akikrasic.ucitavanjezahtevaiodgovora

import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.SlovaKonstante
import java.io.InputStream
import java.io.OutputStream

abstract class UcitavanjeISlanjeNaIzlaz (open val inp:InputStream, open val out: OutputStream){
    fun ucitajteIntIBajt(): UcitaniIntIBajt {
        try {
            val ucitaniInt = inp.read()
            return UcitaniIntIBajt(ucitaniInt)
        }
        catch(e:Exception){
            e.printStackTrace()
            println("Doslo je do greske prilikom ucitavanja")
            return ucitaniIntIBajtMinus1
        }
    }
    val bajtoviZaStampu = mutableListOf<Byte>()
    fun vadjenjeStringaIzTelaIUpisNaStrim(znakZaZaustavljanje:Byte):String{
        val sb = StringBuilder()
        var ucitaniIntIBajt = ucitajteIntIBajt()

        while(ucitaniIntIBajt.ucitaniInt!=-1&& ucitaniIntIBajt.ucitaniBajt!=znakZaZaustavljanje){
            //out.write(ucitaniIntIBajt.ucitaniInt)
            bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
            sb.append(ucitaniIntIBajt.ucitaniKarakter)
            ucitaniIntIBajt = ucitajteIntIBajt()
        }
        bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
        return sb.toString()
    }

    fun samoUcitavanjeIPrepisivanje(){
        try {
            //out.write(inp.read())
            bajtoviZaStampu.add(inp.read().toByte())

        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
    fun ucitavanjePrvogRedaTriVrednosti():TriVrednosti = TriVrednosti(
        vadjenjeStringaIzTelaIUpisNaStrim( SlovaKonstante.prazno),
        vadjenjeStringaIzTelaIUpisNaStrim(SlovaKonstante.prazno),
        vadjenjeStringaIzTelaIUpisNaStrim( SlovaKonstante.krajRedaR)
    )
    fun ucitavanjeHedera(){
        val hederi = HederiCuvanjeIPretraga()
        var ucitaniIntIBajt = ucitajteIntIBajt()
        while(ucitaniIntIBajt.ucitaniInt!=-1 && ucitaniIntIBajt.ucitaniBajt!= SlovaKonstante.krajRedaR) {
            bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
            val hederNaziv = ucitaniIntIBajt.ucitaniKarakter + vadjenjeStringaIzTelaIUpisNaStrim(SlovaKonstante.dveTacke)
            samoUcitavanjeIPrepisivanje()
            val hederVrednost = vadjenjeStringaIzTelaIUpisNaStrim(SlovaKonstante.krajRedaR)
            hederi.dodajteHederIVrednost(hederNaziv, hederVrednost)
            samoUcitavanjeIPrepisivanje()
            ucitaniIntIBajt = ucitajteIntIBajt()



        }
        bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
        vratiteObjekat().hederi = hederi
    }
    fun ucitavanjeNormalnogTelaBajtovi(velicina:Int):ByteArray{
        val niz = ByteArray(velicina)
        for(i in 0..velicina-1){
            val ucitaniIntIBajt = ucitajteIntIBajt()
            niz[i] = ucitaniIntIBajt.ucitaniBajt
            //out.write(ucitaniIntIBajt.ucitaniInt)
            bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
        }
        return niz
    }

    fun ucitavanjeNormalnogTela(){
        vratiteObjekat().telo =  ucitavanjeNormalnogTelaBajtovi(PretvaranjeStringaUBroj.pretvaranjeStringaUBroj(vratiteObjekat().hederi.pretraga(
            HederiNazivi.contentLength)))

    }

    abstract fun vratiteObjekat():ZajednickoZaZahtevIOdgovor
    abstract fun dodeliteTriVrednostiUPrvomRedu(vr:TriVrednosti)
     fun ucitavanjePrvogReda(){
         dodeliteTriVrednostiUPrvomRedu(ucitavanjePrvogRedaTriVrednosti())
     }
    abstract fun ucitavanjeTela()
    fun ucitavanjeISlanjeNaIzlaz(){
        ucitavanjePrvogReda()

        samoUcitavanjeIPrepisivanje()

        ucitavanjeHedera()

        samoUcitavanjeIPrepisivanje()

        ucitavanjeTela()
        out.write(bajtoviZaStampu.toByteArray())
        out.flush()

    }
}