import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
import srb.akikrasic.forma.Forma
import srb.akikrasic.komunikacija.Komunikacija
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.sertifikat.generisanjeSertifikata
import srb.akikrasic.sertifikat.sifra
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

        while (true) {

                val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)

                    ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()
                    println("ime niti je ${Thread.currentThread().name}")

                val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
                //   val dekoder = RadSaDekoderima.vratiteDekoder( zahtev.hederi.pretraga(HederiNazivi.contentEncoding))
                //  println("hederi zahteva  ${zahtev.hederi.mapaOriginalnihHedera}")
                // println("telo zahteva je ${url} : ${String(dekoder.dekodujte(zahtev.telo))}")
                // Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(zahtev.url,String(dekoder.dekodujte(zahtev.telo)) ))
                //  Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev.url, zahtev.metoda, zahtev.hederi,String(dekoder.dekodujte(zahtev.telo)) ))


                val ucitavanjeOdgovoraObjekat = UcitavanjeOdgovoraISlanjeNaIzlaz(serverInput, browserOutput)

                    ucitavanjeOdgovoraObjekat.ucitavanjeISlanjeNaIzlaz()

                val odgovor = ucitavanjeOdgovoraObjekat.vratiteOdgovor()
                //val dekoderOdgovor = RadSaDekoderima.vratiteDekoder((odgovor.hederi.pretraga(HederiNazivi.contentEncoding)))
                //   println("hederi odgovor ${odgovor.hederi.mapaOriginalnihHedera}")

                //   println("telo odgovora je ${url} ${String(dekoderOdgovor.dekodujte(odgovor.telo))}")
                Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev, odgovor))


        }


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
                //val dekoderOdgovor = RadSaDekoderima.vratiteDekoder((odgovor.hederi.pretraga(HederiNazivi.contentEncoding)))
                //   println("hederi odgovor ${odgovor.hederi.mapaOriginalnihHedera}")

                //   println("telo odgovora je ${url} ${String(dekoderOdgovor.dekodujte(odgovor.telo))}")

                val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)

                    ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()

                val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
                //   val dekoder = RadSaDekoderima.vratiteDekoder( zahtev.hederi.pretraga(HederiNazivi.contentEncoding))
                //  println("hederi zahteva  ${zahtev.hederi.mapaOriginalnihHedera}")
                // println("telo zahteva je ${url} : ${String(dekoder.dekodujte(zahtev.telo))}")
                // Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(zahtev.url,String(dekoder.dekodujte(zahtev.telo)) ))
                //  Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev.url, zahtev.metoda, zahtev.hederi,String(dekoder.dekodujte(zahtev.telo)) ))


                Komunikacija.kanalZaKomunikaciju.send(KomunikacijaPodaci(url, zahtev, odgovor))



        }


}


val mapaSoketa = mutableMapOf<String, Socket>()

val provider = "BCJSSE"

fun generisiteNoviSSLSocketKaServeru(urlIPort: UrlIPort): SSLSocket {
    val ssfNova = SSLSocketFactory.getDefault() as SSLSocketFactory
    return ssfNova.createSocket(InetAddress.getByName(urlIPort.url), urlIPort.port) as SSLSocket
}

fun generisiteNoviSoketKaServeru(urlIPort: UrlIPort): Socket {
    if (urlIPort.port == 443) {
        return generisiteNoviSSLSocketKaServeru(urlIPort)
    }
    println("ural i port su ")
    println(urlIPort)
    return Socket(InetAddress.getByName(urlIPort.url), urlIPort.port)
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
fun izdavajanjeUrlaIPortaIzHostaZahteva(zahtev: Zahtev):UrlIPort{
    val urlIPortStringovi= zahtev.hederi.pretraga(HederiNazivi.host).split(":")

    if( urlIPortStringovi.size==1) {
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

        println("prvi zahtev je id:${id} : ${String(ucitaniBajtovi)} ")

        val zahtev = zahtevPrvi.vratiteZahtev()
        if (ucitaniBajtovi.size == 0) {
            return@withContext
        }
        val metoda = zahtev.metoda
        if (metoda == HttpMetodeNazivi.connect) {

            val urlIPort =
                izdavajanjeUrlaIPortaIzHostaZahteva(zahtev)//izdvajanjeUrlaConnect(ucitaniBajtovi) //to je malo suvisno i treba se izbaci


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

            val noviSoket =
                generisiteNoviSSLSocketKaServeru(urlIPort)//mapaSoketa[urlIPort.url]?:generisiteNoviSSLSocketKaServeru(urlIPort)

            upisivanjenaSoketJednePoruke(out, poruka)

            val noviInp = noviSoket.getInputStream()
            val noviOut = noviSoket.getOutputStream()



            mapaSoketa.put(zahtev.hederi.pretraga(HederiNazivi.host), noviSoket)

            ucitavanjeIUpisivanjeZahtevOdgovor(sInp, sOut, noviInp, noviOut, id, urlIPort.url)


        } else {
            println("udje li on ovde uopste")
//        val host = zahtev.hederi.pretraga(HederiNazivi.host)
//        //   println("${zahtev.metoda} ${zahtev.url} ${zahtev.protokolVerzija}")
//        //  println(zahtev.hederi)
//        val hostUrl = VadjenjeIzStringa.podeliteHost(host)
//        println("novi soket da vidimo ${hostUrl} ${host} ")

            val noviSoket = Socket(InetAddress.getByName(zahtev.hederi.pretraga(HederiNazivi.host)), 80)
            //  generisiteNoviSoketKaServeru(hostUrl)// menjano Maj 2024. mapaSoketa[host]?: generisiteNoviSoketKaServeru(hostUrl)
            println(noviSoket)
            upisivanjenaSoketJednePoruke(noviSoket.getOutputStream(), ucitaniBajtovi)
            //upisivanjeNaSoket(noviSoket.getOutputStream(), ucitaniBajtovi)
            // ucitavanjeIUpisivanjePrvoOdgovorPaZahtev(inp, out,noviSoket.getInputStream(), noviSoket.getOutputStream(), id, host )
            ucitavanjeIUpisivanjeOdgovorSaServeraPaZahtev(
                inp,
                out,
                noviSoket.getInputStream(),
                noviSoket.getOutputStream(),
                id,
                zahtev.url
            )
        }
    }
}

val brojac = AtomicInteger(0)


val r = '\r'.toByte()
val H = 'H'.toByte()


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
            /*NOSONAR*/  GlobalScope.launch   (Dispatchers.IO) {
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