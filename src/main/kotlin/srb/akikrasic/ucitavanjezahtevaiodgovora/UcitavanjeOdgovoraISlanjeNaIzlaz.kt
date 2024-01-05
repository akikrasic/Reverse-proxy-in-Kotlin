package srb.akikrasic.ucitavanjezahtevaiodgovora

import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiVrednosti
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.SlovaKonstante
import java.io.InputStream
import java.io.OutputStream




class UcitavanjeOdgovoraISlanjeNaIzlaz(override val inp:InputStream,override val out:OutputStream) : UcitavanjeISlanjeNaIzlaz(inp, out){

    val ceoOdgovor = CeoOdgovor()

    override fun vratiteObjekat(): ZajednickoZaZahtevIOdgovor  = ceoOdgovor
    fun vratiteOdgovor() = ceoOdgovor



    fun ucitavanjeHeksDuzineChunkedOdgovora():Int{
        val ucitanaHeksDuzina = vadjenjeStringaIzTelaIUpisNaStrim(SlovaKonstante.krajRedaR)
        samoUcitavanjeIPrepisivanje()
        return PretvaranjeStringaUBroj.pretvaranjeHeksadecimalnogStringaUBroj(ucitanaHeksDuzina)
    }
    fun ucitavanjeChunkedTela():ByteArray{
        var zbir = 0
        var duzina = ucitavanjeHeksDuzineChunkedOdgovora()
        println("duzina tela za ucitavanje je ${duzina}")
        val listaBajtova = mutableListOf<ByteArray>()
        while(duzina>0){
            zbir+=duzina
            listaBajtova.add(ucitavanjeNormalnogTelaBajtovi(duzina))
            repeat(2){
                samoUcitavanjeIPrepisivanje()
            }
            duzina = ucitavanjeHeksDuzineChunkedOdgovora()
            println("duzina tela za ucitavanje je ${duzina}")

        }
        repeat(2){
            samoUcitavanjeIPrepisivanje()
        }
       // samoUcitavanjeIPrepisivanje()
        val niz = ByteArray(zbir)
        var trenutni = 0
        for( nizIzListe in listaBajtova){
             for (b in nizIzListe){
                  niz[trenutni]=b
                  trenutni++
             }
        }

        return niz
    }

    override fun dodeliteTriVrednostiUPrvomRedu(triVrednosti:TriVrednosti){
        val (v1, v2, v3) = triVrednosti
        ceoOdgovor.protokolVerzija = v1
        ceoOdgovor.statusKod = v2
        ceoOdgovor.statusPoruka = v3
    }
    override fun ucitavanjeTela(){
        if(ceoOdgovor.hederi.pretraga(HederiNazivi.transferEncoding)== HederiVrednosti.chunked){
            ceoOdgovor.telo = ucitavanjeChunkedTela()

        }
        else{
            ucitavanjeNormalnogTela()
        }
    }

}