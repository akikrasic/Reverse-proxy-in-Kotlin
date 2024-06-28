package srb.akikrasic.ucitavanjezahtevaiodgovora

import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.SlovaKonstante

data class UrlIPort(val url:String, val port:Int)

object VadjenjeIzStringa {
    fun podeliteHost(host:String):UrlIPort{
        println("host izgleda ovako ${host} ")
        val niz = host.split(":")
        if(niz.size==2){
            try {
                val port = Integer.valueOf(niz[1])
                return (UrlIPort(niz[0], port))
            }
            catch(e:Exception){
                println("greska kod soket ${host}")
                return UrlIPort("localhost", 80)
            }
        }
        else{
            println("greska kod soket ${host}")
            return UrlIPort("localhost",80)
        }
    }

}