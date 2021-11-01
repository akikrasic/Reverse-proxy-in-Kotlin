import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
import srb.akikrasic.sertifikat.generisanjeSertifikata
import srb.akikrasic.sertifikat.sifra
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.security.SecureRandom
import java.security.Security
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.*
import java.util.zip.GZIPInputStream


val port = 8080
val poruka = "HTTP/1.1 200 Connection established\r\n\r\n".toByteArray()

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
        print(e.printStackTrace())
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

suspend fun ucitavanjeIUpisivanje(inp:InputStream, out:OutputStream, id:Int, tip:Int){
    try {
        var i = 0;
        while(true) {
            val ucitaniBajtoviLista = ucitavanjeBajtovaSaStrima(inp)
            upisivanjeNaSoket(out, ucitaniBajtoviLista)
            GlobalScope.launch (Dispatchers.IO){
                //println(String(ucitaniBajtoviLista))
                kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtoviLista))
            }
        }
    }catch(e:Exception){

    }
}
val provider = "BCJSSE"

suspend fun obradaSoketa(s: Socket) {
    val inp = s.getInputStream()
    val out = s.getOutputStream()
    val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)
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
        ucitavanjeIUpisivanjeGlobalScope(noviInp,sOut,id,1)
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
            if(url[8]>='1' && url[8]<='9'){
                var pozCrte = 8
                while(url[pozCrte]!='/'){
                    pozCrte++
                }
                val adr = url.subSequence(7,pozCrte).toString()
                //println(adr)
                s = Socket(adr,80)
            }else{
                s = Socket(InetAddress.getByName(url),80)
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
data class ObradaZahtevaIliOdgovora(val id:Int, val tip:Int,val bajtovi :ByteArray )

val kanalZaKomunikaciju = Channel<ObradaZahtevaIliOdgovora>(10)
suspend fun obradaPoruke(){
    while(true){
        val o = kanalZaKomunikaciju.receive()
        print(o.id)
        if(o.tip==1){

           GlobalScope.launch(Dispatchers.IO) {
               obradaOdgovora(o.bajtovi)

           }
        }
    }
}

suspend fun obradaOdgovora(bajtovi:ByteArray){
   slanjeZaStampu(bajtovi)
   val r = '\r'.toByte()
    val H = 'H'.toByte()

   val prviRed = StringBuilder()
    slanjeZaStampu(bajtovi)
   for (b in bajtovi) {
       if(b!=r){
           prviRed.append(b.toChar())
       }
       else{
           break;
       }
   }
    if(bajtovi[0]==H){
        slanjeZaStampu("Normalna je poruka".toByteArray())
    }
    else{
        slanjeZaStampu("chunked je zmijata".toByteArray())
    }
    slanjeZaStampu("Prvi red je : ${prviRed.toString()}".toByteArray())

}
val kanalZaStampanje:Channel<ByteArray> = Channel<ByteArray>(10)
suspend fun slanjeZaStampu( niz:ByteArray){
    kanalZaStampanje.send(niz)

}

suspend fun stampanje(){
    while(true){
        println("pocetak")
        val n = kanalZaStampanje.receive()
        println(String(n))
        println("kraj")
    }
}

fun main(args:Array<String>) {

        runBlocking {

            val i = AtomicInteger(0)
            print(i.addAndGet(10))


            Security.addProvider(BouncyCastleJsseProvider())
            Security.addProvider(BouncyCastleFipsProvider())
            val server = ServerSocket(port)
            GlobalScope.launch(Dispatchers.IO) {
                obradaPoruke()
            }
            GlobalScope.launch(Dispatchers.IO) {
                stampanje()
            }

            while (true) {
                val s = server.accept()
                GlobalScope.launch(Dispatchers.IO) {
                    obradaSoketa(s)
                }
            }


        }

}