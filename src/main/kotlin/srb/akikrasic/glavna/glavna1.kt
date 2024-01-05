/*import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
import srb.akikrasic.dekodiranje.Dekoder
import srb.akikrasic.dekodiranje.RadSaDekoderima
import srb.akikrasic.odgovor.Odgovor
import srb.akikrasic.odgovor.chunkedOdgovor
import srb.akikrasic.sertifikat.generisanjeSertifikata
import srb.akikrasic.sertifikat.sifra
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.security.SecureRandom
import java.security.Security
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.*


val port = 8080
val poruka = "HTTP/1.1 200 Connection established\r\n\r\n".toByteArray()
class glavna{}

val mapa = Collections.synchronizedMap(mutableMapOf<Int, MutableList<Odgovor>>())

val radSaDekoderima = RadSaDekoderima()

suspend fun ucitavanjeBajtovaSaStrima(inp: InputStream): ByteArray {
    val ucitaniBajtoviLista = mutableListOf<ByteArray>()
    val velicinaZaUcitavanje = 4096
    var brojUcitanih = 0

    do {

        val bajtovi = ByteArray(velicinaZaUcitavanje)
        brojUcitanih = inp.read(bajtovi)
        ucitaniBajtoviLista.add(bajtovi)


    } while (velicinaZaUcitavanje == brojUcitanih)
    val brojPotpunih = ucitaniBajtoviLista.size-1
    val ukupnaVelicinaNiza = brojPotpunih *velicinaZaUcitavanje+brojUcitanih
    //ubaceno 24.08.2023.
    if(ukupnaVelicinaNiza<0){
        return ByteArray(0)
    }
    val konacniNiz = ByteArray(ukupnaVelicinaNiza)
    var brojac=0
    var i=0
    var j=0
    while(i<brojPotpunih){
        j=0
        while(j<velicinaZaUcitavanje){
            konacniNiz[brojac]=ucitaniBajtoviLista[i][j]
            j++
            brojac++
        }
        i++
    }
    i=0
    while(i<brojUcitanih){
        konacniNiz[brojac]=ucitaniBajtoviLista[brojPotpunih][i]
        i++
        brojac++
    }
    return konacniNiz
}

suspend fun napraviteStringOdListe(listaBajtova: List<ByteArray>): String {
    val sb = StringBuilder()
    for (b in listaBajtova) {
        sb.append(String(b))
    }
    return sb.toString()

}

suspend fun upisivanjenaSoketJednePoruke(out: OutputStream, bajtovi: ByteArray) {
    out.write(bajtovi)
    out.flush()
}

suspend fun upisivanjeNaSoket(out: OutputStream, bajtovi:ByteArray) {

    out.write(bajtovi)

    out.flush()
}

data class UrlIPort(val url: String, val port: Int)

suspend fun izdvajanjePost(bajtovi:ByteArray):String{
    val b = bajtovi
    var kretanjeKrozNiz = 5
    val prazno = ' '.toByte()
    val sbUrl = StringBuilder()
    while(b[kretanjeKrozNiz]!=prazno){
        sbUrl.append(b[kretanjeKrozNiz].toChar())
        kretanjeKrozNiz++
    }
    return sbUrl.toString()
}

suspend fun izdvajanjeGet(bajtovi :ByteArray):String{
   val b = bajtovi
    var kretanjeKrozNiz = 4
    val prazno = ' '.toByte()
    val sbUrl = StringBuilder()
    while(b[kretanjeKrozNiz]!=prazno){
        sbUrl.append(b[kretanjeKrozNiz].toChar())
        kretanjeKrozNiz++
    }
    return sbUrl.toString()
}
suspend fun izdvajanjeUrlaConnect(bajtovi: ByteArray): UrlIPort {
    val b = bajtovi
    var kretanjeKrozNiz = 8
    val sbUrl = StringBuilder()
    val dveTacke = ':'.toByte()
    val prazno = ' '.toByte()
    while (b[kretanjeKrozNiz] != dveTacke) {
        sbUrl.append(b[kretanjeKrozNiz].toChar())
        kretanjeKrozNiz++
    }
    kretanjeKrozNiz++
    var port = 0
    while (b[kretanjeKrozNiz] != prazno) {
        port *= 10
        port += b[kretanjeKrozNiz] - '0'.toByte()
        kretanjeKrozNiz++
    }


    return UrlIPort(sbUrl.toString(), port)
}
suspend fun ucitavanjeIUpisivanjeNoviKoncept(soketKlijent:Socket, soketServer:Socket){
    val inpKlijent = soketKlijent.getInputStream()
    val outKlijent = soketKlijent.getOutputStream()
    val inpServer = soketServer.getInputStream()
    val outServer = soketServer.getOutputStream()
    GlobalScope.launch(Dispatchers.IO) {


        try {
            while (true) {
                val ucitaniBajtoviSaKlijenta = ucitavanjeBajtovaSaStrima(inpKlijent)
                upisivanjeNaSoket(outServer, ucitaniBajtoviSaKlijenta)
                //println(String(ucitaniBajtoviSaKlijenta))
                val ucitaniBajtoviSaServera = ucitavanjeBajtovaSaStrima(inpServer)
                upisivanjeNaSoket(outKlijent, ucitaniBajtoviSaServera)
               // println(String(ucitaniBajtoviSaServera))
            }
        } catch (e: Exception) {
        println(e.printStackTrace())
        }
    }
}
suspend fun ucitavanjeIUpisivanjeGlobalScope(inp:InputStream, out:OutputStream, id:Int, tip:Int){
    GlobalScope.launch(Dispatchers.IO){
        ucitavanjeIUpisivanje(inp, out,id, tip)
    }

}
suspend fun stampanje(bajtovi:ByteArray){
    val s = String(bajtovi)
    print(s)

}
suspend fun ucitavanjeIUpisivanjeSamo(inp:InputStream, out:OutputStream, id:Int, tip:Int, dekoder:Dekoder){
    try {

        while(true) {
            //tu treba da ne saljem dekoder dalje jer ne treba ali je problem sto ne radi nista

            val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)

            upisivanjeNaSoket(out, ucitaniBajtovi)
            GlobalScope.launch (Dispatchers.IO){
                //println(String(ucitaniBajtoviLista))
                kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
            }
        }
    }catch(e:Exception){

    }
}
val razmak = ' '.toByte()
val dveTacke = ':'.toByte()

data class PronadjenoUPoruci(val stigloSeDo:Int, val izdvojeno:String)
fun izdvajanjeIzNiza(bajtovi:ByteArray, pocetak:Int):ByteArray{
    var velicinaNiza = bajtovi.size-pocetak
    if(velicinaNiza<0){
        velicinaNiza = 0
    }
    val novi = ByteArray(velicinaNiza)
    var j = 0
    for (i in pocetak..bajtovi.lastIndex){
        novi[j] =bajtovi[i]
        j++
    }
    return novi
}
fun izvadiStringINastavi(bajtovi: ByteArray, dokSeNeNadje:Byte, brojacPocetak:Int):PronadjenoUPoruci{
    var brojac = brojacPocetak
    val sb = StringBuilder()
    while(brojac<bajtovi.size && bajtovi[brojac]!=dokSeNeNadje ){
        sb.append(bajtovi[brojac].toChar())
        brojac++
    }
    brojac++
    return PronadjenoUPoruci(brojac, sb.toString())
}
fun napraviteOdgovor(bajtovi:ByteArray): Odgovor {
    if(bajtovi.size==0){
        return Odgovor(false, "","","", mutableMapOf(), bajtovi)
    }
    if(bajtovi[0]==H){
        /*val sbVerzija = StringBuilder()
        var brojac = 0
        while(brojac<bajtovi.size && bajtovi[brojac]!=razmak){
            sbVerzija.append(bajtovi[brojac])
            brojac++
        }
        brojac++
        val verzija = sbVerzija.toString()
        val sbStatus = StringBuilder()
        while(brojac<bajtovi.size && bajtovi[brojac]!=razmak){
            sbStatus.append(bajtovi[brojac])
            brojac++
        }
        brojac++
        val status = sbStatus.toString()
        val sbPoruka = StringBuilder()
        while(brojac<bajtovi.size && bajtovi[brojac]!=r){
            sbPoruka.append(bajtovi[brojac])
            brojac++
        }
        val poruka= sbPoruka.toString()
        brojac+=2
        while(bajtovi[brojac]!=r){
            val sbHederKljuc = StringBuilder()
            while(brojac<bajtovi.size && bajtovi[brojac]!=dveTacke){
                sbHederKljuc.append(bajtovi[brojac])
            }
        }
        */
        var brojac = 0
        val (brojacPosleVerzije, verzija) = izvadiStringINastavi(bajtovi, razmak, brojac)
        val (brojacPosleStatusa, status) = izvadiStringINastavi(bajtovi,razmak, brojacPosleVerzije )
        val ( brojacPoslePoruke, poruka) = izvadiStringINastavi(bajtovi, r, brojacPosleStatusa)
        brojac = brojacPoslePoruke+1
        val mapa = mutableMapOf<String, String>()
        while(brojac<bajtovi.size&& bajtovi[brojac]!=r){
            val (brojacPosleHedera, headerKljuc) = izvadiStringINastavi(bajtovi, dveTacke, brojac)
            val (brojacPosleVrednosti, headerVrednost) = izvadiStringINastavi(bajtovi, r, brojacPosleHedera)
            brojac=brojacPosleVrednosti+1
            mapa[headerKljuc]=headerVrednost
        }
        brojac+=2

        val telo = izdvajanjeIzNiza(bajtovi, brojac)
        //val telo1 = bajtovi.sliceArray(brojac..bajtovi.lastIndex-2)
        return Odgovor(false,verzija, status, poruka, mapa, telo )

    }
    else{
        var brojac = 0
        while(brojac<bajtovi.size  && bajtovi[brojac]!=r ){
            brojac++
        }
        brojac+=2
        val telo = izdvajanjeIzNiza(bajtovi, brojac)
        return chunkedOdgovor(telo)

    }

}
suspend fun ucitavanjeIUpisivanje(inp:InputStream, out:OutputStream, id:Int, tip:Int){
    if(tip==1){
        try{
            val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)
            upisivanjeNaSoket(out, ucitaniBajtovi)
            GlobalScope.launch(Dispatchers.IO){

            }
            val odgovor = napraviteOdgovor(ucitaniBajtovi)
            val tipEnkondinga = odgovor.headeri.getOrDefault("Content-Encoding", "").trim()
            val dekoder = radSaDekoderima.vratiteDekoder(tipEnkondinga)
            kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
            kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
            ucitavanjeIUpisivanjeSamo(inp, out, id, tip, dekoder)
            GlobalScope.launch(Dispatchers.IO){
                dodavanjeUMapuIProveraDaLiDaSeStampa(id, odgovor, dekoder)
            }

        }
        catch( e:Exception){
            e.printStackTrace()
        }
    }
    else{
        ucitavanjeIUpisivanjeSamo(inp, out, id,tip, radSaDekoderima.prazanDekoder)
    }
}
fun dodavanjeUMapuIProveraDaLiDaSeStampa(id:Int, bajtovi:ByteArray, dekoder:Dekoder){
    dodavanjeUMapuIProveraDaLiDaSeStampa(id, napraviteOdgovor(bajtovi), dekoder)
}
fun dodavanjeUMapuIProveraDaLiDaSeStampa(id:Int, odgovor:Odgovor, dekoder:Dekoder){
    if(odgovor.telo.size==0){
        val sviBajtovi = sabiranjeOdgovora(id)
        println("konacno dekodovano i sabrano: ${dekoder.dekodujteUString(sviBajtovi)}")
        //glupo
        if(mapa.containsKey(id)) {
            mapa[id]!!.clear()
        }
        return
    }
     dodavanjeUMapu(id, odgovor)
}

fun dodavanjeUMapu(id:Int, odgovor:Odgovor){
 if(!mapa.containsKey(id)){
     mapa[id] = mutableListOf<Odgovor>()
 }
   mapa[id]!!.add(odgovor)
}



fun sabiranjeOdgovora(id:Int):ByteArray{
    val listaNizovaBajtova = mapa.get(id)?:mutableListOf()
    var sumaVelicina = 0

    listaNizovaBajtova.forEach { sumaVelicina+=it.telo.size }

    val noviNiz = ByteArray(sumaVelicina)
    var j = 0
    listaNizovaBajtova.forEach {
        for( i in 0..it.telo.lastIndex){
            noviNiz[j]= it.telo[i]
        }
    }
    return noviNiz
}


val provider = "BCJSSE"

suspend fun obradaSoketa(s: Socket) {
    val inp = s.getInputStream()
    val out = s.getOutputStream()
    val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)

    if(ucitaniBajtovi.size==0){
        return
    }
    println("prvi put ${String(ucitaniBajtovi)}")


    if (ucitaniBajtovi[0] == 'C'.toByte()) {
        val urlIPort = izdvajanjeUrlaConnect(ucitaniBajtovi)
        val sslContext = SSLContext.getInstance("TLS", provider)
        val ks = generisanjeSertifikata(urlIPort.url)
        val kmf = KeyManagerFactory.getInstance("PKIX", provider)
        kmf.init(ks, sifra)
        sslContext.init(kmf.keyManagers, null, SecureRandom())
        val ssf = sslContext.socketFactory
        val sslSocket = ssf.createSocket(s, inp, true) as SSLSocket
        val sInp = sslSocket.getInputStream()
        val sOut = sslSocket.getOutputStream()
        sslSocket.useClientMode=false

        val protokoli = arrayOf<String>("TLSv1.3")
        val cipherSuites = arrayOf<String>("TLS_AES_256_GCM_SHA384")
        val ssfNova = SSLSocketFactory.getDefault() as SSLSocketFactory
        val noviSoket  = ssfNova.createSocket(InetAddress.getByName(urlIPort.url), urlIPort.port) as SSLSocket

        upisivanjenaSoketJednePoruke(out, poruka)

        val noviInp = noviSoket.getInputStream()
        val noviOut = noviSoket.getOutputStream()
        val kanalZaKomunikaciju = Channel<ByteArray>()
        val id = brojac.addAndGet(1)
       ucitavanjeIUpisivanjeGlobalScope(sInp, noviOut,id, 0)
        ucitavanjeIUpisivanjeGlobalScope(noviInp,sOut,id,1 )
        //ucitavanjeIUpisivanjeNoviKoncept(sslSocket, noviSoket)
        }
    else{
        if(ucitaniBajtovi[0]=='G'.toByte()){
            val url = izdvajanjeGet(ucitaniBajtovi)
            //println(String(ucitaniBajtovi))
            var s:Socket = Socket()//ajde glupoi je ali samo da proradi
            if(url[8]>='1' && url[8]<='9'){
                var pozCrte = 8
                while(url[pozCrte]!='/'){
                    pozCrte++
                }
                val adr = url.subSequence(7,pozCrte).toString()
               // println(adr)
                s = Socket(adr,80)
            }else{
                s = Socket(InetAddress.getByName(url),80)
            }
            //val inetAdresa = InetAddress.getByName(url)

            val noviSoket =s// Socket(inetAdresa, 80)
            val noviInp = noviSoket.getInputStream()
            val noviOut  = noviSoket.getOutputStream()
            upisivanjeNaSoket(noviOut,ucitaniBajtovi)
            val kanalZaKomunikaciju = Channel<ByteArray>()
            val id = brojac.addAndGet(1)
            ucitavanjeIUpisivanjeGlobalScope(inp, noviOut,id, 0)
            ucitavanjeIUpisivanjeGlobalScope(noviInp, out,id, 1)

            //ucitavanjeIUpisivanjeNoviKoncept(s,noviSoket)

        }else{
           // println("POST JE :")
            //println(String(ucitaniBajtovi))
            //println(napraviteStringOdListe(ucitaniBajtoviLista))
            //println(String(ucitaniBajtovi))
                var kompir=""
                try {
                    var url1 = izdvajanjePost(ucitaniBajtovi)
                    kompir = url1
                }catch(e:ArrayIndexOutOfBoundsException){
                    println("kompir")
                }
            val url = kompir
            //println("url")
           // println(url)
            var s:Socket = Socket()//ajde glupoi je ali samo da proradi
            println("s POST je $url")
            if(url.trim()==""){
                return
            }
            if(url[8]>='1' && url[8]<='9'){
                var pozCrte = 8
                while(url[pozCrte]!='/'){
                    pozCrte++
                }
                val adr = url.subSequence(7,pozCrte).toString()
                //println(adr)
                //2023. provera mozda tu gresim treba na 443
                //2023 interesantno gmail ne radi a linked in radi brzo i mondo radi kurir
                //2023 izgleda je obrnuto ono sto nije radelo radi i obratno za 443
                s = Socket(adr,80)//80
            }else{
                s = Socket(InetAddress.getByName(url),80)//80
            }
            //val inetAdresa = InetAddress.getByName(url)
            val noviSoket = s//Socket(inetAdresa, 80)
            val noviInp = noviSoket.getInputStream()
            val noviOut  = noviSoket.getOutputStream()
            //println(ucitaniBajtovi)
            upisivanjeNaSoket(noviOut, ucitaniBajtovi)
            val kanalZaKomunikaciju = Channel<ByteArray>()
            val id = brojac.addAndGet(1)
            ucitavanjeIUpisivanjeGlobalScope(inp, noviOut,id,0)
            ucitavanjeIUpisivanjeGlobalScope(noviInp, out,id,1)
           // ucitavanjeIUpisivanjeNoviKoncept(s,noviSoket)
        }
    }
}
val brojac = AtomicInteger(0)
data class ObradaZahtevaIliOdgovora(val id:Int, val tip:Int,val bajtovi :ByteArray, val dekoder:Dekoder )

val kanalZaKomunikaciju = Channel<ObradaZahtevaIliOdgovora>(10)
suspend fun obradaPoruke(){
    while(true){
        val o = kanalZaKomunikaciju.receive()

        if(o.tip==1){

           dodavanjeUMapuIProveraDaLiDaSeStampa(o.id, o.bajtovi, o.dekoder)

        }
    }
}
val r = '\r'.toByte()
val H = 'H'.toByte()


val kanalZaStampanje:Channel<ByteArray> = Channel<ByteArray>(10)
val kanalZaStampanjeUFajl:Channel<ByteArray> = Channel<ByteArray>(100)
suspend fun slanjeZaStampu( niz:ByteArray){
    kanalZaStampanje.send(niz)
    //malo da isprobam
    kanalZaStampanjeUFajl.send(niz)
}
suspend fun slanjeZaStampu(s:String){
    slanjeZaStampu(s.toByteArray())
}

suspend fun stampanje(){
    while(true){
      //  println("pocetak")
        val n = kanalZaStampanje.receive()
       // println(String(n))
       // println("kraj")
    }
}

suspend fun stampanjeUFajl(){
    val datum = LocalDateTime.now()
    val imeFajla = datum.format(DateTimeFormatter.ofPattern("ddMMyyyyhhmmss"))
    val out = FileWriter(File("/home/aki/mojProksi/pokretanje${imeFajla}"))
    while(true){
        val poruka = kanalZaStampanjeUFajl.receive()
        out.write(String(poruka))
        out.flush()
    }

}

fun main(args:Array<String>) {

        runBlocking {

            val i = AtomicInteger(0)

            Security.addProvider(BouncyCastleJsseProvider())
            Security.addProvider(BouncyCastleFipsProvider())
            val server = ServerSocket(port)
            GlobalScope.launch(Dispatchers.IO) {
                obradaPoruke()
            }
            GlobalScope.launch(Dispatchers.IO) {
                stampanje()
            }

            GlobalScope.launch(Dispatchers.IO){
                stampanjeUFajl()
            }

            while (true) {
                val s = server.accept()
                GlobalScope.launch(Dispatchers.IO) {
                    obradaSoketa(s)
                }
            }


        }

}*/