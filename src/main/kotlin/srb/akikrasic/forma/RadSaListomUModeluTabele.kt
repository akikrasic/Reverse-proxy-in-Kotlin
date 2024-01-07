package srb.akikrasic.forma

import kotlinx.coroutines.sync.Mutex
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import java.util.*

class RadSaListomUModeluTabele {



    val listaSvih = Collections.synchronizedList(mutableListOf<KomunikacijaPodaci>())
    var listaZaPrikaz = listaSvih
    var zaPretragu:String= ""
    val mutex = Mutex()

    suspend fun pretraga(zaPretragu:String){
        mutex.lock()
        this.zaPretragu = zaPretragu
        if(zaPretragu==""){
            listaZaPrikaz = listaSvih
            mutex.unlock()
            return
        }
        val rezultat = listaSvih.filter { it.host.contains(zaPretragu) }
        listaZaPrikaz = rezultat
        mutex.unlock()
    }
    suspend fun dodajteUListu(k:KomunikacijaPodaci){
        mutex.lock()
        if(zaPretragu!=""){
            if(k.host.contains(zaPretragu)){
                listaZaPrikaz.add(k)
            }
        }
        listaSvih.add(k)
        mutex.unlock()
    }

}