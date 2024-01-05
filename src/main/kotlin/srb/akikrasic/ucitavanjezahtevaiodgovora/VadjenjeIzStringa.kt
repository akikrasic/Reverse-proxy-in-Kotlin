package srb.akikrasic.ucitavanjezahtevaiodgovora

import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.SlovaKonstante

data class UrlIPort(val url:String, val port:Int)

object VadjenjeIzStringa {
    fun podeliteHost(host:String):UrlIPort{
        val niz = host.split(":")
        if(niz.size==2){
            try {
                val port = Integer.valueOf(niz[1])
                return (UrlIPort(niz[0], port))
            }
            catch(e:Exception){
                return UrlIPort("localhost", 80)
            }
        }
        else{
            return UrlIPort("localhost",80)
        }
    }

}