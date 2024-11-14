package srb.akikrasic.ucitavanjezahtevaiodgovora

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import srb.akikrasic.ucitavanjeWebSocketa.UcitavanjeZajednicko
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.SlovaKonstante
import java.io.InputStream
import java.io.OutputStream

abstract class UcitavanjeISlanjeNaIzlaz (inp:InputStream, out: OutputStream):UcitavanjeZajednicko(inp, out){

    val bajtoviZaStampu = mutableListOf<Byte>()
    suspend fun vadjenjeStringaIzTelaIUpisNaStrim(znakZaZaustavljanje:Byte):String{
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

    suspend fun samoUcitavanjeIPrepisivanje(){
        try {
          //  withContext(Dispatchers.IO) {
                //out.write(inp.read())
                bajtoviZaStampu.add(inp.read().toByte())
            //}
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
    suspend fun ucitavanjePrvogRedaTriVrednosti():TriVrednosti = TriVrednosti(
        vadjenjeStringaIzTelaIUpisNaStrim( SlovaKonstante.prazno),
        vadjenjeStringaIzTelaIUpisNaStrim(SlovaKonstante.prazno),
        vadjenjeStringaIzTelaIUpisNaStrim( SlovaKonstante.krajRedaR)
    )
    suspend fun ucitavanjeHedera(){
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
    suspend fun ucitavanjeNormalnogTelaBajtovi(velicina:Int):ByteArray{
        val niz = ByteArray(velicina)
        for(i in 0..velicina-1){
            val ucitaniIntIBajt = ucitajteIntIBajt()
            niz[i] = ucitaniIntIBajt.ucitaniBajt
            //out.write(ucitaniIntIBajt.ucitaniInt)
            bajtoviZaStampu.add(ucitaniIntIBajt.ucitaniBajt)
        }
        return niz
    }

    suspend fun ucitavanjeNormalnogTela(){
        vratiteObjekat().telo =  ucitavanjeNormalnogTelaBajtovi(PretvaranjeStringaUBroj.pretvaranjeStringaUBroj(vratiteObjekat().hederi.pretraga(
            HederiNazivi.contentLength)))

    }

    abstract fun vratiteObjekat():ZajednickoZaZahtevIOdgovor
    abstract fun dodeliteTriVrednostiUPrvomRedu(vr:TriVrednosti)
     suspend fun ucitavanjePrvogReda(){
         dodeliteTriVrednostiUPrvomRedu(ucitavanjePrvogRedaTriVrednosti())
     }
    abstract suspend fun ucitavanjeTela()
    suspend fun ucitavanjeISlanjeNaIzlaz(){
            ucitavanjePrvogReda()

            samoUcitavanjeIPrepisivanje()

            ucitavanjeHedera()

            samoUcitavanjeIPrepisivanje()

            ucitavanjeTela()
            out.write(bajtoviZaStampu.toByteArray())
            out.flush()
    }
}