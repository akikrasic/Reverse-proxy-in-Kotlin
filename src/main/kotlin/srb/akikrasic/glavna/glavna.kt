import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
import srb.akikrasic.forma.Forma
import srb.akikrasic.komunikacija.Komunikacija
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.sertifikat.generisanjeSertifikata
import srb.akikrasic.sertifikat.sifra
import srb.akikrasic.ucitavanjeWebSocketa.UcitavanjeWebSocketa
import srb.akikrasic.ucitavanjezahtevaiodgovora.UcitavanjeOdgovoraISlanjeNaIzlaz
import srb.akikrasic.ucitavanjezahtevaiodgovora.UcitavanjeZahtevaISlanjeNaIzlaz
import srb.akikrasic.ucitavanjezahtevaiodgovora.UrlIPort
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HttpMetodeNazivi
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.security.KeyStore
import java.security.SecureRandom
import java.security.Security
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.swing.SwingUtilities


val port = 8080
val poruka = "HTTP/1.1 200 Connection established\r\n\r\n".toByteArray()
val porukaWebSocket = "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\n\r\n".toByteArray()

val stringZaWebSocket="258EAFA5-E914-47DA-95CA-C5AB0DC85B11"


suspend fun upisivanjenaSoketJednePoruke(out: OutputStream, bajtovi: ByteArray) {
    withContext(Dispatchers.IO) {
        out.write(bajtovi)

        out.flush()
    }
}

suspend fun upisivanjeNaSoket(out: OutputStream, bajtovi: ByteArray) {
    withContext(Dispatchers.IO) {
        out.write(bajtovi)
        kanalZaStampanjeUFajl.send(bajtovi)
        out.flush()
    }
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


suspend fun ucitavanjeIUpisivanjeZahtevOdgovor(
    browserInput: InputStream,
    browserOutput: OutputStream,
    serverInput: InputStream,
    serverOutput: OutputStream,
    id: Int,
    url: String
) {
        var put = 1
        while (true) {

            val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)

           // try {
                ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()
            println("Ovo je ${put}. ${url} zahtev je ${ucitavanjeZahtevaObjekat}")
          //  }
//            catch (e:Exception){
//                println("Greska uhvacena u zahtev: ${url} ${e.message} ${e.stackTraceToString()}")
//            }

            val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
           // val hederiLista = zahtev.hederi.mapaOriginalnihHedera.map { "${it.key}: ${it.value }"}
//            println("""
//               ${zahtev.metoda} ${zahtev.url}
//               ${hederiLista}
//            """.trimIndent())

            val ucitavanjeOdgovoraObjekat = UcitavanjeOdgovoraISlanjeNaIzlaz(serverInput, browserOutput)

          //  try {
                ucitavanjeOdgovoraObjekat.ucitavanjeISlanjeNaIzlaz()
           // }
//            catch (e:Exception){
//                println("Greska uhvacena u odgovor: ${url} ${e.message} ${e.stackTraceToString()}")
//
//            }
            val odgovor = ucitavanjeOdgovoraObjekat.vratiteOdgovor()
            if( odgovor.statusKod>="300" && odgovor.statusKod<"400"){
                println("""
                    
                    300 je
                    ${odgovor.statusKod}
                    ${odgovor.telo}
                    ${zahtev.url}
                    
                """.trimIndent())
            }

            Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev, odgovor))
            if( odgovor.hederi.pretraga(HederiNazivi.connection)=="close"){
                break
            }
            if( zahtev.hederi.pretraga("Upgrade")=="websocket"){
               // upisivanjenaSoketJednePoruke(browserOutput, porukaWebSocket)
                println("""U pitanju je websocket ${url}
                    zahtev: ${ucitavanjeZahtevaObjekat} 
                    odgovor: ${ucitavanjeOdgovoraObjekat}
                    
                    """)
                GlobalScope.launch(mojDispecer) {
                    nastavakWebSocket(serverInput, browserOutput, url, "server")
                }
                GlobalScope.launch (mojDispecer){ nastavakWebSocket(browserInput, serverOutput, url, "klijent")  }
                break
            }
            put++
        }



}

suspend fun nastavakWebSocket(ulaz:InputStream, izlaz:OutputStream, url:String, koSalje:String ){

        UcitavanjeWebSocketa(ulaz, izlaz, url, koSalje).ucitajteWebSocketPorukuIPrepisite();

}
suspend fun ucitavanjeIUpisivanjeOdgovorSaServeraPaZahtev(
    browserInput: InputStream,
    browserOutput: OutputStream,
    serverInput: InputStream,
    serverOutput: OutputStream,
    id: Int,
    url: String
) {
    while (true) {

        val ucitavanjeOdgovoraObjekat = UcitavanjeOdgovoraISlanjeNaIzlaz(serverInput, browserOutput)
        ucitavanjeOdgovoraObjekat.ucitavanjeISlanjeNaIzlaz()
        val odgovor = ucitavanjeOdgovoraObjekat.vratiteOdgovor()
        val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)
        ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()
        val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
        Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev, odgovor))

    }

}


val mapaSoketa = mutableMapOf<String, Socket>()

val provider = "BCJSSE"

fun generisiteNoviSSLSocketKaServeru(urlIPort: UrlIPort): SSLSocket {
    val ssfNova = SSLSocketFactory.getDefault() as SSLSocketFactory
    val soket =  ssfNova.createSocket(InetAddress.getByName(urlIPort.url), urlIPort.port) as SSLSocket
    return soket
}

val mapaSertifikata = mutableMapOf<String, KeyStore>()
fun vratiteSertifikat(url: String): KeyStore {
    if (mapaSertifikata.containsKey(url)) {
        return mapaSertifikata[url]!!
    } else {
        val sertifikat = generisanjeSertifikata(url)
        mapaSertifikata[url] = sertifikat
        return sertifikat
    }
}

fun izdavajanjeUrlaIPortaIzHostaZahteva(zahtev: Zahtev): UrlIPort {
    val urlIPortStringovi = zahtev.hederi.pretraga(HederiNazivi.host).split(":")

    if (urlIPortStringovi.size == 1) {
        return UrlIPort(urlIPortStringovi[0], 80)
    }
    return UrlIPort(urlIPortStringovi[0], urlIPortStringovi[1].toInt())
}

suspend fun obradaSoketa2(s: Socket) {
    withContext(mojDispecer) {
        val id = brojac.addAndGet(1)
        val inp = s.getInputStream()
        val out = s.getOutputStream()
        val bajtoviOut = ByteArrayOutputStream()
        val zahtevPrvi = UcitavanjeZahtevaISlanjeNaIzlaz(inp, bajtoviOut)
        zahtevPrvi.ucitavanjeISlanjeNaIzlaz()
        val ucitaniBajtovi = bajtoviOut.toByteArray()

     //   println("prvi zahtev je id:${id} : ${String(ucitaniBajtovi)} ")

        val zahtev = zahtevPrvi.vratiteZahtev()
        if (ucitaniBajtovi.size == 0) {
            return@withContext
        }
        val metoda = zahtev.metoda

        val urlIPort =
            izdavajanjeUrlaIPortaIzHostaZahteva(zahtev)//izdvajanjeUrlaConnect(ucitaniBajtovi) //to je malo suvisno i treba se izbaci
        println("""
            Ucitani bajtovi: 
            ${String(ucitaniBajtovi)}""".trimIndent())
        if (metoda == HttpMetodeNazivi.connect || urlIPort.port==443 ) {

            if( metoda!=HttpMetodeNazivi.connect){
                println("znaci nije connect a 443 je ")
            }

            val sslContext = SSLContext.getInstance("TLSv1.3", provider)//TLS
            val ks = vratiteSertifikat(urlIPort.url)
            val kmf = KeyManagerFactory.getInstance("PKIX", provider)
            kmf.init(ks, sifra)
            sslContext.init(kmf.keyManagers, null, SecureRandom())
            val ssf = sslContext.socketFactory
            val sslSocket = ssf.createSocket(s, inp, true) as SSLSocket
            val sInp = sslSocket.getInputStream()

            val sOut = sslSocket.getOutputStream()
            sslSocket.useClientMode = false
          //  println("sslSoket ${urlIPort.url} ${sslSocket.enabledProtocols.toList()} ${sslSocket.enabledCipherSuites.toList()}")
            val noviSoket =
                generisiteNoviSSLSocketKaServeru(urlIPort)//mapaSoketa[urlIPort.url]?:generisiteNoviSSLSocketKaServeru(urlIPort)

            upisivanjenaSoketJednePoruke(out, poruka)

            val noviInp = noviSoket.getInputStream()
            val noviOut = noviSoket.getOutputStream()



            mapaSoketa.put(zahtev.hederi.pretraga(HederiNazivi.host), noviSoket)

            ucitavanjeIUpisivanjeZahtevOdgovor(sInp, sOut, noviInp, noviOut, id, urlIPort.url)


        } else {
          // println("udje li on ovde uopste ${urlIPort.url} ${urlIPort.port} ${String(ucitaniBajtovi)}")

            val noviSoket = Socket(InetAddress.getByName(zahtev.hederi.pretraga(HederiNazivi.host)), 80
            )
          //  println(noviSoket)
            upisivanjenaSoketJednePoruke(noviSoket.getOutputStream(), ucitaniBajtovi)
           ucitavanjeIUpisivanjeZahtevOdgovor(s.getInputStream(), s.getOutputStream(), noviSoket.getInputStream(), noviSoket.getOutputStream(), id, urlIPort.url)
//         //           ucitavanjeIUpisivanjeOdgovorSaServeraPaZahtev(
//                inp,
//                out,
//                noviSoket.getInputStream(),
//                noviSoket.getOutputStream(),
//                id,
//                zahtev.url
//            )
        }
    }
}

val brojac = AtomicInteger(0)

val kanalZaStampanje: Channel<ByteArray> = Channel<ByteArray>(10)
val kanalZaStampanjeUFajl: Channel<ByteArray> = Channel<ByteArray>(100)
suspend fun slanjeZaStampu(niz: ByteArray) {
    kanalZaStampanje.send(niz)
    //malo da isprobam
    kanalZaStampanjeUFajl.send(niz)
}

suspend fun stampanjeUFajl() {
    val datum = LocalDateTime.now()
    val imeFajla = datum.format(DateTimeFormatter.ofPattern("ddMMyyyyhhmmss"))
    val out = FileWriter(File("/home/aki/mojProksi/pokretanje${imeFajla}"))
    while (true) {
        val poruka = kanalZaStampanjeUFajl.receive()
        out.write(String(poruka))
        out.flush()
    }

}

val mojDispecer = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
fun main(args: Array<String>) {
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
    runBlocking {
        SwingUtilities.invokeLater {
            Forma()
        }

        Security.addProvider(BouncyCastleJsseProvider())
        Security.addProvider(BouncyCastleFipsProvider())
        val server = ServerSocket(port)
//            GlobalScope.launch(Dispatchers.IO) {
//                try {
//                    obradaPoruke()
//                }
//                catch(e:Exception){
//                    println("greska uvacena u obradu poruke")
//                    e.printStackTrace()
//                }
//
//            }

//
//        GlobalScope.launch(Dispatchers.IO) {
//            stampanjeUFajl()
//        }


        while (true) {
            val s = server.accept()
            /*NOSONAR*/  GlobalScope.launch(mojDispecer) { //tu sam izmenio 24. avgust 2024 bilo je Dispatchers.IO
                
                try {
                    obradaSoketa2(s)
                } catch (e: Exception) {
                    println("greska uvacena u soket")
                    e.printStackTrace()
                }
            }
        }


    }

}