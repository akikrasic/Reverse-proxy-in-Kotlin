//import kotlinx.coroutines.*
//import kotlinx.coroutines.channels.Channel
//import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
//import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
//import srb.akikrasic.dekodiranje.Dekoder
//import srb.akikrasic.dekodiranje.RadSaDekoderima
//import srb.akikrasic.forma.Forma
//import srb.akikrasic.odgovor.Odgovor
//import srb.akikrasic.odgovor.chunkedOdgovor
//import srb.akikrasic.sertifikat.generisanjeSertifikata
//import srb.akikrasic.sertifikat.sifra
//import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HederiNazivi
//import srb.akikrasic.ucitavanjezahtevaiodgovora.UcitavanjeOdgovoraISlanjeNaIzlaz
//import srb.akikrasic.ucitavanjezahtevaiodgovora.UcitavanjeZahtevaISlanjeNaIzlaz
//import srb.akikrasic.ucitavanjezahtevaiodgovora.UrlIPort
//import srb.akikrasic.ucitavanjezahtevaiodgovora.VadjenjeIzStringa
//import srb.akikrasic.ucitavanjezahtevaiodgovora.konstante.HttpMetodeNazivi
//import java.io.*
//import java.net.InetAddress
//import java.net.ServerSocket
//import java.net.Socket
//import java.security.KeyStore
//import java.security.SecureRandom
//import java.security.Security
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.util.*
//import java.util.concurrent.atomic.AtomicInteger
//import javax.net.ssl.*
//import javax.swing.SwingUtilities
//
//
//val port = 8080
//val poruka = "HTTP/1.1 200 Connection established\r\n\r\n".toByteArray()
//class glavna{}
//
//val mapa = Collections.synchronizedMap(mutableMapOf<Int, MutableList<Odgovor>>())
//
//
//suspend fun ucitavanjeBajtovaSaStrima1(inp:InputStream):ByteArray =ByteArray(0)
//
//suspend fun ucitavanjeBajtovaSaStrimaIUpisivanjeNaStrim(inp1:InputStream, out:OutputStream):List<ByteArray>{
//    val inp = BufferedInputStream(inp1)
//
//    return listOf(ByteArray(0))
//
//}
//suspend fun ucitavanjeBajtovaSaStrima(inp1: InputStream): ByteArray {
//    val inp = BufferedInputStream(inp1)
//    println(Thread.currentThread())
//    val ucitaniBajtoviLista = mutableListOf<ByteArray>()
//    val velicinaZaUcitavanje = 4096
//    var brojUcitanih = 0
//    println("dostupno: ${inp.available()}")
//    do {
//
//        val bajtovi = ByteArray(velicinaZaUcitavanje)
//     //   println("available ${inp.available()}")
//     //   while(inp.available()==0){
//      //      delay(100L)
//      //     println("gotova pauza")
//      //  }
//
//        brojUcitanih = inp.read(bajtovi)
//       // println("broj ucitanih $brojUcitanih")
//        ucitaniBajtoviLista.add(bajtovi)
//
//
//    } while (velicinaZaUcitavanje == brojUcitanih)
//    val brojPotpunih = ucitaniBajtoviLista.size-1
//    val ukupnaVelicinaNiza = brojPotpunih *velicinaZaUcitavanje+brojUcitanih
//    println("ukupno ucitano: ${ukupnaVelicinaNiza} a sad je dostupno ${inp.available()}")
//    //ubaceno 24.08.2023.
//    if(ukupnaVelicinaNiza<0){
//        return ByteArray(0)
//    }
//    val konacniNiz = ByteArray(ukupnaVelicinaNiza)
//    var brojac=0
//    var i=0
//    var j=0
//    while(i<brojPotpunih){
//        j=0
//        while(j<velicinaZaUcitavanje){
//            konacniNiz[brojac]=ucitaniBajtoviLista[i][j]
//            j++
//            brojac++
//        }
//        i++
//    }
//    i=0
//    while(i<brojUcitanih){
//        konacniNiz[brojac]=ucitaniBajtoviLista[brojPotpunih][i]
//        i++
//        brojac++
//    }
//    return konacniNiz
//}
//
//suspend fun napraviteStringOdListe(listaBajtova: List<ByteArray>): String {
//    val sb = StringBuilder()
//    for (b in listaBajtova) {
//        sb.append(String(b))
//    }
//    return sb.toString()
//
//}
//
//suspend fun upisivanjenaSoketJednePoruke(out: OutputStream, bajtovi: ByteArray) {
//    out.write(bajtovi)
//    out.flush()
//}
//
//suspend fun upisivanjeNaSoket(out: OutputStream, bajtovi:ByteArray) {
//
//    out.write(bajtovi)
//
//    out.flush()
//}
//
//
//
//suspend fun izdvajanjePost(bajtovi:ByteArray):String{
//    val b = bajtovi
//    var kretanjeKrozNiz = 5
//    val prazno = ' '.toByte()
//    val sbUrl = StringBuilder()
//    while(b[kretanjeKrozNiz]!=prazno){
//        sbUrl.append(b[kretanjeKrozNiz].toChar())
//        kretanjeKrozNiz++
//    }
//    return sbUrl.toString()
//}
//
//suspend fun izdvajanjeGet(bajtovi :ByteArray):String{
//   val b = bajtovi
//    var kretanjeKrozNiz = 11
//    val prazno = '/'.toByte()
//    val sbUrl = StringBuilder()
//    while(b[kretanjeKrozNiz]!=prazno){
//        sbUrl.append(b[kretanjeKrozNiz].toChar())
//        kretanjeKrozNiz++
//    }
//    return sbUrl.toString()
//}
//suspend fun izdvajanjeUrlaConnect(bajtovi: ByteArray): UrlIPort {
//    val b = bajtovi
//    var kretanjeKrozNiz = 8
//    val sbUrl = StringBuilder()
//    val dveTacke = ':'.toByte()
//    val prazno = ' '.toByte()
//    while (b[kretanjeKrozNiz] != dveTacke) {
//        sbUrl.append(b[kretanjeKrozNiz].toChar())
//        kretanjeKrozNiz++
//    }
//    kretanjeKrozNiz++
//    var port = 0
//    while (b[kretanjeKrozNiz] != prazno) {
//        port *= 10
//        port += b[kretanjeKrozNiz] - '0'.toByte()
//        kretanjeKrozNiz++
//    }
//
//
//    return UrlIPort(sbUrl.toString(), port)
//}
//suspend fun ucitavanjeIUpisivanjeNoviKoncept(soketKlijent:Socket, soketServer:Socket){
//    val inpKlijent = soketKlijent.getInputStream()
//    val outKlijent = soketKlijent.getOutputStream()
//    val inpServer = soketServer.getInputStream()
//    val outServer = soketServer.getOutputStream()
//    GlobalScope.launch(Dispatchers.IO) {
//
//
//        try {
//            while (true) {
//                val ucitaniBajtoviSaKlijenta = ucitavanjeBajtovaSaStrima(inpKlijent)
//                upisivanjeNaSoket(outServer, ucitaniBajtoviSaKlijenta)
//                //println(String(ucitaniBajtoviSaKlijenta))
//                val ucitaniBajtoviSaServera = ucitavanjeBajtovaSaStrima(inpServer)
//                upisivanjeNaSoket(outKlijent, ucitaniBajtoviSaServera)
//               // println(String(ucitaniBajtoviSaServera))
//            }
//        } catch (e: Exception) {
//        println(e.printStackTrace())
//        }
//    }
//}
//suspend fun ucitavanjeIUpisivanjeGlobalScope(inp:InputStream, out:OutputStream, id:Int, tip:Int){
//    GlobalScope.launch(Dispatchers.IO){
//        ucitavanjeIUpisivanje(inp, out,id, tip)
//    }
//
//}
//suspend fun stampanje(bajtovi:ByteArray){
//    val s = String(bajtovi)
//    print(s)
//
//}
//suspend fun ucitavanjeIUpisivanjeSamo(inp:InputStream, out:OutputStream, id:Int, tip:Int, dekoder:Dekoder){
//    try {
//
//        while(true) {
//            //tu treba da ne saljem dekoder dalje jer ne treba ali je problem sto ne radi nista
//
//            val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)
//            println("Ucitani bajtovi: ${String(ucitaniBajtovi)}")
//            upisivanjeNaSoket(out, ucitaniBajtovi)
//            GlobalScope.launch (Dispatchers.IO){
//                //println(String(ucitaniBajtoviLista))
//                kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
//            }
//        }
//    }catch(e:Exception){
//        println("riknul je u ucitavanjeIUpisivanje samo")
//    }
//}
//val razmak = ' '.toByte()
//val dveTacke = ':'.toByte()
//
//data class PronadjenoUPoruci(val stigloSeDo:Int, val izdvojeno:String)
//fun izdvajanjeIzNiza(bajtovi:ByteArray, pocetak:Int):ByteArray{
//    var velicinaNiza = bajtovi.size-pocetak
//    if(velicinaNiza<0){
//        velicinaNiza = 0
//    }
//    val novi = ByteArray(velicinaNiza)
//    var j = 0
//    for (i in pocetak..bajtovi.lastIndex){
//        novi[j] =bajtovi[i]
//        j++
//    }
//    return novi
//}
//fun izvadiStringINastavi(bajtovi: ByteArray, dokSeNeNadje:Byte, brojacPocetak:Int):PronadjenoUPoruci{
//    var brojac = brojacPocetak
//    val sb = StringBuilder()
//    while(brojac<bajtovi.size && bajtovi[brojac]!=dokSeNeNadje ){
//        sb.append(bajtovi[brojac].toChar())
//        brojac++
//    }
//    brojac++
//    return PronadjenoUPoruci(brojac, sb.toString().trim())
//}
//fun napraviteOdgovor(bajtovi:ByteArray): Odgovor {
//    if(bajtovi.size==0){
//        return Odgovor(false, "","","", mutableMapOf(), bajtovi)
//    }
//    if(bajtovi[0]==H){
//        /*val sbVerzija = StringBuilder()
//        var brojac = 0
//        while(brojac<bajtovi.size && bajtovi[brojac]!=razmak){
//            sbVerzija.append(bajtovi[brojac])
//            brojac++
//        }
//        brojac++
//        val verzija = sbVerzija.toString()
//        val sbStatus = StringBuilder()
//        while(brojac<bajtovi.size && bajtovi[brojac]!=razmak){
//            sbStatus.append(bajtovi[brojac])
//            brojac++
//        }
//        brojac++
//        val status = sbStatus.toString()
//        val sbPoruka = StringBuilder()
//        while(brojac<bajtovi.size && bajtovi[brojac]!=r){
//            sbPoruka.append(bajtovi[brojac])
//            brojac++
//        }
//        val poruka= sbPoruka.toString()
//        brojac+=2
//        while(bajtovi[brojac]!=r){
//            val sbHederKljuc = StringBuilder()
//            while(brojac<bajtovi.size && bajtovi[brojac]!=dveTacke){
//                sbHederKljuc.append(bajtovi[brojac])
//            }
//        }
//        */
//        var brojac = 0
//        val (brojacPosleVerzije, verzija) = izvadiStringINastavi(bajtovi, razmak, brojac)
//        val (brojacPosleStatusa, status) = izvadiStringINastavi(bajtovi,razmak, brojacPosleVerzije )
//        val ( brojacPoslePoruke, poruka) = izvadiStringINastavi(bajtovi, r, brojacPosleStatusa)
//        brojac = brojacPoslePoruke+1
//        val mapa = mutableMapOf<String, String>()
//        while(brojac<bajtovi.size&& bajtovi[brojac]!=r){
//            val (brojacPosleHedera, headerKljuc) = izvadiStringINastavi(bajtovi, dveTacke, brojac)
//            val (brojacPosleVrednosti, headerVrednost) = izvadiStringINastavi(bajtovi, r, brojacPosleHedera)
//            brojac=brojacPosleVrednosti+1
//            mapa[headerKljuc]=headerVrednost
//        }
//        brojac+=2
//
//        val telo = izdvajanjeIzNiza(bajtovi, brojac)
//        //val telo1 = bajtovi.sliceArray(brojac..bajtovi.lastIndex-2)
//        return Odgovor(false,verzija, status, poruka, mapa, telo )
//
//    }
//    else{
//        var brojac = 0
//        while(brojac<bajtovi.size  && bajtovi[brojac]!=r ){
//            brojac++
//        }
//        brojac+=2
//        val telo = izdvajanjeIzNiza(bajtovi, brojac)
//        return chunkedOdgovor(telo)
//
//    }
//
//}
//suspend fun ucitavanjeIUpisivanje(inp:InputStream, out:OutputStream, id:Int, tip:Int){
//    if(tip==1){
//        try{
//            val ucitaniBajtovi = ucitavanjeBajtovaSaStrima(inp)
//            upisivanjeNaSoket(out, ucitaniBajtovi)
//            GlobalScope.launch(Dispatchers.IO){
//
//            }
//            val odgovor = napraviteOdgovor(ucitaniBajtovi)
//            val tipEnkondinga = odgovor.headeri.getOrDefault("Content-Encoding", "").trim()
//            val dekoder = RadSaDekoderima.vratiteDekoder(tipEnkondinga)
//            kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
//            kanalZaKomunikaciju.send(ObradaZahtevaIliOdgovora(id, tip, ucitaniBajtovi, dekoder))
//            GlobalScope.launch(Dispatchers.IO){
//                dodavanjeUMapuIProveraDaLiDaSeStampa(id, odgovor, dekoder)
//            }
//            ucitavanjeIUpisivanjeSamo(inp, out, id, tip, dekoder)
//
//
//        }
//        catch( e:Exception){
//            e.printStackTrace()
//        }
//    }
//    else{
//        ucitavanjeIUpisivanjeSamo(inp, out, id,tip, RadSaDekoderima.prazanDekoder)
//    }
//}
//suspend fun dodavanjeUMapuIProveraDaLiDaSeStampa(id:Int, bajtovi:ByteArray, dekoder:Dekoder){
//    dodavanjeUMapuIProveraDaLiDaSeStampa(id, napraviteOdgovor(bajtovi), dekoder)
//}
//suspend fun dodavanjeUMapuIProveraDaLiDaSeStampa(id:Int, odgovor:Odgovor, dekoder:Dekoder){
//    println("hederi ${odgovor.headeri}")
//    println("odgovor ${String(odgovor.telo)}")
//    if(odgovor.chunked && odgovor.telo.size==0){
//        val sviBajtovi = sabiranjeOdgovora(id)
//        slanjeZaStampu("konacno dekodovano i sabrano: ${dekoder.dekodujteUString(sviBajtovi)}")
//        //glupo
//        if(mapa.containsKey(id)) {
//            mapa[id]!!.clear()
//        }
//        return
//    }
//    if(odgovor.chunked){
//        dodavanjeUMapu(id, odgovor)
//    }
//    if(odgovor.headeri.containsKey(transferEncoding) && odgovor.headeri[transferEncoding]!!.trim()=="chunked"){
//        dodavanjeUMapu(id, odgovor)
//    }
//    else{
//        slanjeZaStampu("jedan odgovor: ${dekoder.dekodujteUString(odgovor.telo)}")
//    }
//
//}
//
//fun dodavanjeUMapu(id:Int, odgovor:Odgovor){
// if(!mapa.containsKey(id)){
//     mapa[id] = mutableListOf<Odgovor>()
// }
//   mapa[id]!!.add(odgovor)
//}
//
//
//val transferEncoding = "Transfer-Encoding"
//val chunked = "chunked"
//val contentEncoding = "Content-Encoding"
//val contentLength = "Content-Length"
//
//
//suspend fun ucitavanjeIUpisivanjeUCiklus(browserInput: InputStream, browserOutput: OutputStream, serverInput: InputStream, serverOutput: OutputStream, id:Int){
//
//}
//
//suspend fun  ucitavanjeIUpisivanjeRedom(browserInput:InputStream, browserOutput:OutputStream, serverInput:InputStream, serverOutput:OutputStream, id:Int){
//    GlobalScope.launch {
//        while (true) {
//            val ucitaniBajtoviSaBrowsera = ucitavanjeBajtovaSaStrima(browserInput)
//            upisivanjeNaSoket(serverOutput, ucitaniBajtoviSaBrowsera)
//            println("id: $id browser: ${String(ucitaniBajtoviSaBrowsera)}")
//            val ucitaniBajtoviSaServera = ucitavanjeBajtovaSaStrima(serverInput)
//            upisivanjeNaSoket(browserOutput, ucitaniBajtoviSaServera)
//            val odgovor = napraviteOdgovor(ucitaniBajtoviSaServera)
//            var duzinaOdgovora = 0
//            if (odgovor.headeri.containsKey(contentLength)) {
//                try {
//                    duzinaOdgovora = Integer.parseInt(odgovor.headeri[contentLength])
//                } catch (е: Exception) {
//                    println("greska prilikom konvertovanja content length")
//                }
//            }
//            println("duzina odgovora je $duzinaOdgovora")
//            println("id: $id odgovor sa servera: ${String(ucitaniBajtoviSaServera)}")
//            var zbir = odgovor.telo.size
//            val listaOdgovora  = mutableListOf(odgovor)
//            while(zbir<duzinaOdgovora){
//                val ucitaniBajtoviPonovoSaServera = ucitavanjeBajtovaSaStrima(serverInput)
//                upisivanjeNaSoket(browserOutput, ucitaniBajtoviPonovoSaServera)
//                println("ucitani bajtovi sa servera ponovo: $zbir $duzinaOdgovora ${String(ucitaniBajtoviPonovoSaServera)}")
//                zbir+=ucitaniBajtoviPonovoSaServera.size
//            }
//            println("zbir na kraju je $zbir a $duzinaOdgovora")
//
//        }
//    }
//}
//
//
//fun sabiranjeOdgovora(id:Int):ByteArray{
//    val listaNizovaBajtova = mapa[id]?:mutableListOf()
//    var sumaVelicina = 0
//
//    listaNizovaBajtova.forEach { sumaVelicina+=it.telo.size }
//
//    val noviNiz = ByteArray(sumaVelicina)
//    var j = 0
//    listaNizovaBajtova.forEach {
//        for( i in 0..it.telo.lastIndex){
//            noviNiz[j]= it.telo[i]
//        }
//    }
//    return noviNiz
//}
//
//
// fun ucitavanjeIUpisivanjeZahtevOdgovor(browserInput:InputStream, browserOutput:OutputStream, serverInput:InputStream, serverOutput:OutputStream, id:Int, url:String){
//    GlobalScope.launch(Dispatchers.IO){
//        while(true){
//
//            val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)
//            ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()
//            val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
//            val dekoder = RadSaDekoderima.vratiteDekoder( zahtev.hederi.pretraga(HederiNazivi.contentEncoding))
//            println("hederi zahteva  ${zahtev.hederi.mapaOriginalnihHedera}")
//            println("telo zahteva je ${url} : ${String(dekoder.dekodujte(zahtev.telo))}")
//
//            val ucitavanjeOdgovoraObjekat = UcitavanjeOdgovoraISlanjeNaIzlaz(serverInput, browserOutput)
//            ucitavanjeOdgovoraObjekat.ucitavanjeISlanjeNaIzlaz()
//            val odgovor = ucitavanjeOdgovoraObjekat.vratiteOdgovor()
//            val dekoderOdgovor = RadSaDekoderima.vratiteDekoder((odgovor.hederi.pretraga(HederiNazivi.contentEncoding)))
//            println("hederi odgovor ${odgovor.hederi.mapaOriginalnihHedera}")
//
//            println("telo odgovora je ${url} ${String(dekoderOdgovor.dekodujte(odgovor.telo))}")
//
//        }
//
//    }
//}
//
//suspend fun ucitavanjeIUpisivanjePrvoOdgovorPaZahtev(browserInput:InputStream, browserOutput:OutputStream, serverInput:InputStream, serverOutput:OutputStream, id:Int){
//    GlobalScope.launch(Dispatchers.IO){
//        var brojac =0
//        while(true){
//            println("ucitao je od browser zahtev broj: ${brojac}")
//            val ucitavanjeOdgovoraObjekat = UcitavanjeOdgovoraISlanjeNaIzlaz(serverInput, browserOutput)
//            ucitavanjeOdgovoraObjekat.ucitavanjeISlanjeNaIzlaz()
//            val odgovor = ucitavanjeOdgovoraObjekat.vratiteOdgovor()
//            val dekoderOdgovor = RadSaDekoderima.vratiteDekoder((odgovor.hederi.pretraga(HederiNazivi.contentEncoding)))
//            println("telo odgovora je ${String(dekoderOdgovor.dekodujte(odgovor.telo))}")
//
//
//            val ucitavanjeZahtevaObjekat = UcitavanjeZahtevaISlanjeNaIzlaz(browserInput, serverOutput)
//            ucitavanjeZahtevaObjekat.ucitavanjeISlanjeNaIzlaz()
//            val zahtev = ucitavanjeZahtevaObjekat.vratiteZahtev()
//            val dekoder = RadSaDekoderima.vratiteDekoder( zahtev.hederi.pretraga(HederiNazivi.contentEncoding))
//            println("telo zahteva je : ${String(dekoder.dekodujte(zahtev.telo))}")
//
//            brojac++
//
//        }
//
//    }
//}
//
//
//
//val mapaSoketa = mutableMapOf<String, Socket>()
//
//val provider = "BCJSSE"
//
//fun generisiteNoviSSLSocketKaServeru(urlIPort: UrlIPort):SSLSocket{
//    val ssfNova = SSLSocketFactory.getDefault() as SSLSocketFactory
//    return ssfNova.createSocket(InetAddress.getByName(urlIPort.url), urlIPort.port) as SSLSocket
//}
//
//fun generisiteNoviSoketKaServeru(urlIPort: UrlIPort):Socket{
//    if(urlIPort.port==443){
//        return generisiteNoviSSLSocketKaServeru(urlIPort)
//    }
//    return Socket(InetAddress.getByName(urlIPort.url), urlIPort.port)
//}
//val mapaSertifikata = mutableMapOf<String, KeyStore>()
//fun vratiteSertifikat(url:String):KeyStore{
//    if(mapaSertifikata.containsKey(url)){
//        return mapaSertifikata[url]!!
//    }
//    else{
//        val sertifikat = generisanjeSertifikata(url)
//        mapaSertifikata[url]= sertifikat
//        return sertifikat
//    }
//}
//suspend fun obradaSoketa2(s:Socket){
//    val id = brojac.addAndGet(1)
//    val inp = s.getInputStream()
//    val out = s.getOutputStream()
//    val bajtoviOut = ByteArrayOutputStream()
//    val zahtevPrvi = UcitavanjeZahtevaISlanjeNaIzlaz(inp, bajtoviOut)
//    zahtevPrvi.ucitavanjeISlanjeNaIzlaz()
//    val ucitaniBajtovi = bajtoviOut.toByteArray()
//
//    println("prvi zahtev je id:${id} : ${String(ucitaniBajtovi)} ")
//
//    val zahtev = zahtevPrvi.vratiteZahtev()
//    if(ucitaniBajtovi.size==0){
//        return
//    }
//    val metoda = zahtev.metoda
//    if(metoda == HttpMetodeNazivi.connect){
//        val urlIPort = izdvajanjeUrlaConnect(ucitaniBajtovi) //to je malo suvisno i treba se izbaci
//
//
//        val sslContext = SSLContext.getInstance("TLSv1.3", provider)//TLS
//        val ks = vratiteSertifikat(urlIPort.url)
//        val kmf = KeyManagerFactory.getInstance("PKIX", provider)
//        kmf.init(ks, sifra)
//        sslContext.init(kmf.keyManagers, null, SecureRandom())
//        val ssf = sslContext.socketFactory
//        val sslSocket = ssf.createSocket(s, inp, true) as SSLSocket
//        val sInp = sslSocket.getInputStream()
//        val sOut = sslSocket.getOutputStream()
//        sslSocket.useClientMode=false
//
//        val noviSoket = generisiteNoviSSLSocketKaServeru(urlIPort)//mapaSoketa[urlIPort.url]?:generisiteNoviSSLSocketKaServeru(urlIPort)
//
//        upisivanjenaSoketJednePoruke(out, poruka)
//
//        val noviInp = noviSoket.getInputStream()
//        val noviOut = noviSoket.getOutputStream()
//
//
//
//        mapaSoketa.put(zahtev.hederi.pretraga(HederiNazivi.host), noviSoket)
//
//        ucitavanjeIUpisivanjeZahtevOdgovor(sInp, sOut, noviInp, noviOut, id, urlIPort.url)
//
//
//    }
//    else{
//        println("udje li on ovde uopste")
//        val host = zahtev.hederi.pretraga(HederiNazivi.host)
//     //   println("${zahtev.metoda} ${zahtev.url} ${zahtev.protokolVerzija}")
//      //  println(zahtev.hederi)
//        val hostUrl = VadjenjeIzStringa.podeliteHost(host)
//
//        val noviSoket = mapaSoketa[host]?: generisiteNoviSoketKaServeru(hostUrl)
//        upisivanjeNaSoket(noviSoket.getOutputStream(), ucitaniBajtovi)
//        ucitavanjeIUpisivanjePrvoOdgovorPaZahtev(inp, out,noviSoket.getInputStream(), noviSoket.getOutputStream(), id )
//    }
//}
//suspend fun obradaSoketa(s: Socket) {
//    val id = brojac.addAndGet(1)
//    val inp = s.getInputStream()
//    val out = s.getOutputStream()
//    val bajtoviOut = ByteArrayOutputStream()
//    val zahtevPrvi = UcitavanjeZahtevaISlanjeNaIzlaz(inp, bajtoviOut)
//    zahtevPrvi.ucitavanjeISlanjeNaIzlaz()
//    val ucitaniBajtovi = bajtoviOut.toByteArray()
//
//    if(ucitaniBajtovi.size==0){
//        return
//    }
//    println("prvi put id je ${id} ${String(ucitaniBajtovi)}")
//
//
//    if (ucitaniBajtovi[0] == 'C'.toByte()) {
//        val urlIPort = izdvajanjeUrlaConnect(ucitaniBajtovi)
//        val sslContext = SSLContext.getInstance("TLS", provider)
//        val ks = generisanjeSertifikata(urlIPort.url)
//        val kmf = KeyManagerFactory.getInstance("PKIX", provider)
//        kmf.init(ks, sifra)
//        sslContext.init(kmf.keyManagers, null, SecureRandom())
//        val ssf = sslContext.socketFactory
//        val sslSocket = ssf.createSocket(s, inp, true) as SSLSocket
//        val sInp = sslSocket.getInputStream()
//        val sOut = sslSocket.getOutputStream()
//        sslSocket.useClientMode=false
//
//        val protokoli = arrayOf<String>("TLSv1.3")
//        val cipherSuites = arrayOf<String>("TLS_AES_256_GCM_SHA384")
//        val ssfNova = SSLSocketFactory.getDefault() as SSLSocketFactory
//        val noviSoket  = ssfNova.createSocket(InetAddress.getByName(urlIPort.url), urlIPort.port) as SSLSocket
//
//       // noviSoket.startHandshake() // dodato oktobar 2023
//        upisivanjenaSoketJednePoruke(out, poruka)
//
//        val noviInp = noviSoket.getInputStream()
//        val noviOut = noviSoket.getOutputStream()
//        val kanalZaKomunikaciju = Channel<ByteArray>()
//
//        ucitavanjeIUpisivanjeZahtevOdgovor(sInp, sOut, noviInp, noviOut, id, urlIPort.url)
//       // ucitavanjeIUpisivanjeRedom(sInp, sOut, noviInp, noviOut, id)
//      // ucitavanjeIUpisivanjeGlobalScope(sInp, noviOut,id, 0)
//       // ucitavanjeIUpisivanjeGlobalScope(noviInp,sOut,id,1 )
//        //ucitavanjeIUpisivanjeNoviKoncept(sslSocket, noviSoket)
//        }
//    else{
//        if(ucitaniBajtovi[0]=='G'.toByte()){
//            val url = izdvajanjeGet(ucitaniBajtovi)
//            //println(String(ucitaniBajtovi))
//            var s:Socket = Socket()//ajde glupoi je ali samo da proradi
//           /* if(url[8]>='1' && url[8]<='9'){
//                var pozCrte = 8
//                while(url[pozCrte]!='/'){
//                    pozCrte++
//                }
//                val adr = url.subSequence(7,pozCrte).toString()
//               // println(adr)
//                s = Socket(adr,80)
//            }else{
//                s = Socket(InetAddress.getByName(url),80)
//            }*/
//            //val inetAdresa = InetAddress.getByName(url)
//        //izgleda da je ovo pogresno ja pravim prazan soket!!!!!!!
//            val noviSoket =s// Socket(inetAdresa, 80)
//            val noviInp = noviSoket.getInputStream()
//            val noviOut  = noviSoket.getOutputStream()
//            upisivanjeNaSoket(noviOut,ucitaniBajtovi)
//            val kanalZaKomunikaciju = Channel<ByteArray>()
//           // ne treba vise uospte val id = brojac.addAndGet(1)
//            ucitavanjeIUpisivanjeZahtevOdgovor(inp, out, noviInp, noviOut, id, url)
//
//            // ucitavanjeIUpisivanjeGlobalScope(inp, noviOut,id, 0)
//           // ucitavanjeIUpisivanjeGlobalScope(noviInp, out,id, 1)
//
//            //ucitavanjeIUpisivanjeNoviKoncept(s,noviSoket)
//
//        }else{
//            if(ucitaniBajtovi.size==0){
//                return
//            }
//           // println("POST JE :")
//            //println(String(ucitaniBajtovi))
//            //println(napraviteStringOdListe(ucitaniBajtoviLista))
//            //println(String(ucitaniBajtovi))
//                var kompir=""
//                try {
//                    var url1 = izdvajanjePost(ucitaniBajtovi)
//                    kompir = url1
//                }catch(e:ArrayIndexOutOfBoundsException){
//                    println("kompir")
//                }
//            val url = kompir
//            //println("url")
//           // println(url)
//            var s:Socket = Socket()//ajde glupoi je ali samo da proradi
//            println("s POST je $url")
//            if(url.trim()==""){
//                return
//            }
//            if(url[8]>='1' && url[8]<='9'){
//                var pozCrte = 8
//                while(url[pozCrte]!='/'){
//                    pozCrte++
//                }
//                val adr = url.subSequence(7,pozCrte).toString()
//                //println(adr)
//                //2023. provera mozda tu gresim treba na 443
//                //2023 interesantno gmail ne radi a linked in radi brzo i mondo radi kurir
//                //2023 izgleda je obrnuto ono sto nije radelo radi i obratno za 443
//                s = Socket(adr,80)//80
//            }else{
//                s = Socket(InetAddress.getByName(url),80)//80
//            }
//            //val inetAdresa = InetAddress.getByName(url)
//            val noviSoket = s//Socket(inetAdresa, 80)
//            val noviInp = noviSoket.getInputStream()
//            val noviOut  = noviSoket.getOutputStream()
//            //println(ucitaniBajtovi)
//            upisivanjeNaSoket(noviOut, ucitaniBajtovi)
//            val kanalZaKomunikaciju = Channel<ByteArray>()
//            val id = brojac.addAndGet(1)
//            ucitavanjeIUpisivanjeZahtevOdgovor(inp, out, noviInp, noviOut, id, url)
//
//            // ucitavanjeIUpisivanjeGlobalScope(inp, noviOut,id,0)
//           // ucitavanjeIUpisivanjeGlobalScope(noviInp, out,id,1)
//           // ucitavanjeIUpisivanjeNoviKoncept(s,noviSoket)
//        }
//    }
//}
//val brojac = AtomicInteger(0)
//data class ObradaZahtevaIliOdgovora(val id:Int, val tip:Int,val bajtovi :ByteArray, val dekoder:Dekoder )
//
//val kanalZaKomunikaciju = Channel<ObradaZahtevaIliOdgovora>(10)
//suspend fun obradaPoruke(){
//    while(true){
//        val o = kanalZaKomunikaciju.receive()
//
//        if(o.tip==1){
//
//           dodavanjeUMapuIProveraDaLiDaSeStampa(o.id, o.bajtovi, o.dekoder)
//
//        }
//    }
//}
//val r = '\r'.toByte()
//val H = 'H'.toByte()
//
//
//val kanalZaStampanje:Channel<ByteArray> = Channel<ByteArray>(10)
//val kanalZaStampanjeUFajl:Channel<ByteArray> = Channel<ByteArray>(100)
//suspend fun slanjeZaStampu( niz:ByteArray){
//    kanalZaStampanje.send(niz)
//    //malo da isprobam
//    kanalZaStampanjeUFajl.send(niz)
//}
//suspend fun slanjeZaStampu(s:String){
//    slanjeZaStampu(s.toByteArray())
//}
//
//suspend fun stampanje(){
//    while(true){
//      //  println("pocetak")
//        val n = kanalZaStampanje.receive()
//       // println(String(n))
//       // println("kraj")
//    }
//}
//
//suspend fun stampanjeUFajl(){
//    val datum = LocalDateTime.now()
//    val imeFajla = datum.format(DateTimeFormatter.ofPattern("ddMMyyyyhhmmss"))
//    val out = FileWriter(File("/home/aki/mojProksi/pokretanje${imeFajla}"))
//    while(true){
//        val poruka = kanalZaStampanjeUFajl.receive()
//        out.write(String(poruka))
//        out.flush()
//    }
//
//}
//
//fun main(args:Array<String>) {
//
//        runBlocking {
//            SwingUtilities.invokeLater{
//                Forma()
//            }
//            val i = AtomicInteger(0)
//
//            Security.addProvider(BouncyCastleJsseProvider())
//            Security.addProvider(BouncyCastleFipsProvider())
//            val server = ServerSocket(port)
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
//            GlobalScope.launch(Dispatchers.IO) {
//                stampanje()
//            }
//
//            GlobalScope.launch(Dispatchers.IO){
//                stampanjeUFajl()
//            }
//
//            while (true) {
//                val s = server.accept()
//              /*NOSONAR*/  GlobalScope.launch(Dispatchers.IO) {
//                    try {
//                        obradaSoketa2(s)
//                    }catch(e:Exception){
//                        println("greska uvacena u soket")
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//
//
//
//        }
//
//}