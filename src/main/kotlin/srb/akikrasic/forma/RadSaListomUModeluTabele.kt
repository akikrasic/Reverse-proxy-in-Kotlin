package srb.akikrasic.forma

import kotlinx.coroutines.sync.Mutex
import srb.akikrasic.forma.paneli.PrenosInformacijaZaPretragu
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import java.util.*

class RadSaListomUModeluTabele {



    val listaSvih = Collections.synchronizedList(mutableListOf<KomunikacijaPodaci>())
    var listaZaPrikaz = listaSvih
    var zaPretragu:PrenosInformacijaZaPretragu= PrenosInformacijaZaPretragu("","","")
    val mutex = Mutex()

    suspend fun pretraga(zaPretragu:PrenosInformacijaZaPretragu){
        mutex.lock()
        this.zaPretragu = zaPretragu
        if(proveraDaLiJeZaPretraguSkrozPrazan(zaPretragu)){
            listaZaPrikaz = listaSvih
            mutex.unlock()
            return
        }

        val rezultat = listaSvih.filter { proveraPretraga(zaPretragu, it)}
        listaZaPrikaz = rezultat
        mutex.unlock()
    }
    suspend fun dodajteUListu(k:KomunikacijaPodaci){
        mutex.lock()
        if( proveraDaLiJeZaPretraguSkrozPrazan(zaPretragu)){
            listaSvih.add(k)
            mutex.unlock()
            return
        }
        if(proveraPretraga(zaPretragu, k)){
            listaZaPrikaz.add(k)
        }
        listaSvih.add(k)
        mutex.unlock()
    }

    fun proveraPretraga(zaPretragu: PrenosInformacijaZaPretragu, komunikacija:KomunikacijaPodaci):Boolean =
                    proveraPretragaPojedinacna(zaPretragu.host, komunikacija.host) &&
                    proveraPretragaPojedinacna(zaPretragu.url, komunikacija.zahtev.url) &&
                    proveraPretragaPojedinacna(zaPretragu.metoda, komunikacija.zahtev.metoda)

    fun proveraPretragaPojedinacna(jedanIzZaPretragu:String,izKomunikacije:String ):Boolean{
        if( jedanIzZaPretragu==""){
            return true
        }
        
        return izKomunikacije.lowercase().contains( jedanIzZaPretragu.lowercase())
    }
    fun proveraDaLiJeZaPretraguSkrozPrazan(zaPretragu:PrenosInformacijaZaPretragu) =
        zaPretragu.url=="" && zaPretragu.host=="" && zaPretragu.metoda==""

}