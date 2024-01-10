package srb.akikrasic.forma

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import srb.akikrasic.forma.paneli.PrenosInformacijaZaPretragu
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.ucitavanjezahtevaiodgovora.CeoOdgovor
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev

class RadSaListomUModeluTabeleTest{

    val rad = RadSaListomUModeluTabele()
    val broj = 5
    val google = "https://www.google.com"
    val facebook = "https://www.facebook.com"
    val leetcode = "https://www.leetcode.com"
    val tiktok = "tiktok.com"
    val instagram = "instagram.com"
    val get = "GET"
    val post = "POST"
    private fun napraviteZahtev( url:String, metoda:String):Zahtev{
        val z = Zahtev()
        z.url = url
        z.metoda = metoda
        return z
    }

    private fun napraviteKomunikaciju(host:String, url:String, metoda:String)
    = KomunikacijaPodaci(host, napraviteZahtev(url, metoda), CeoOdgovor())

    private fun napravitePraznuKomunikaciju() = napraviteKomunikaciju("","","")
    @Test
    fun dodavanjeSamoPraznih()= runTest {

        repeat(broj){
            rad.dodajteUListu(napravitePraznuKomunikaciju())
        }
        assertEquals(broj, rad.listaZaPrikaz.size)
    }

    private suspend fun dodajteURadNekolikoPrePretrage(){

        rad.dodajteUListu(napraviteKomunikaciju(google, "/search1", get ))
        rad.dodajteUListu(napraviteKomunikaciju(facebook, "p", post))
        rad.dodajteUListu(napraviteKomunikaciju(leetcode, "1000", get))
        rad.dodajteUListu(napraviteKomunikaciju(google, "a1", post))
        rad.dodajteUListu(napraviteKomunikaciju(leetcode, "bab2000", post))
    }
    suspend fun posaljiteParametreIOcekujteBrojRezultata(host:String, url:String, metoda:String, brojRezultata:Int){
        rad.pretraga(PrenosInformacijaZaPretragu(host, url, metoda))
        assertEquals(brojRezultata, rad.listaZaPrikaz.size)
    }
    @Test
    fun dodavanjeIPretraga()= runTest{
        dodajteURadNekolikoPrePretrage()
        rad.pretraga(PrenosInformacijaZaPretragu("g", "",""))
        assertEquals(2, rad.listaZaPrikaz.size)
        assertEquals(broj, rad.listaSvih.size)
        rad.dodajteUListu(napraviteKomunikaciju(instagram, "c1", get))
        rad.dodajteUListu(napraviteKomunikaciju(tiktok, "p30", post))
        assertEquals(3, rad.listaZaPrikaz.size)
        assertEquals(7,rad.listaSvih.size)

        posaljiteParametreIOcekujteBrojRezultata("","","",7)
        posaljiteParametreIOcekujteBrojRezultata("","", get,3)
        posaljiteParametreIOcekujteBrojRezultata("","", post,4)
        posaljiteParametreIOcekujteBrojRezultata("g", "1", "e",2)
        posaljiteParametreIOcekujteBrojRezultata("g", "1", "e",2)
        posaljiteParametreIOcekujteBrojRezultata("t","0", "st", 2)
    }

}