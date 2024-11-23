package srb.akikrasic.ucitavanjeWebSocketa

import srb.akikrasic.komunikacija.Komunikacija
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.Inflater
import kotlin.experimental.xor

class UcitavanjeWebSocketa(val ulaz: InputStream, val izlaz: OutputStream, val url:String, val saljeKlijent:Boolean): UcitavanjeZajednicko(ulaz, izlaz) {


    private val koSalje = if (saljeKlijent) {
        "klijent"
    } else {
        "server"
    }

    suspend fun ucitajteIPrepisiteDodatneBajtoveZaDuzinu(brojBajtova: Int): Int {

        var duzina: Int = 0
        for (i in 0..brojBajtova - 1) {
            val ucitaniIntIBajt = ucitajteIntIBajt()
            izlaz.write(ucitaniIntIBajt.ucitaniInt)

            duzina = duzina shl 8
            //   duzina+=ucitaniIntIBajt.ucitaniInt
            duzina = duzina or ucitaniIntIBajt.ucitaniInt// 1011_1000 0000_0000
        }//                                                 0000_0000 1100_0011
        //                                                  1011_1000 1100_0011
        return duzina
    }

    fun daLiJeBajtPostavljen(broj: Int, bajt: Int): Boolean = (broj and bajt) == bajt

    suspend fun ucitajteWebSocketPorukuIPrepisite() {
        var listaBajtovaIzPoruke = mutableListOf<Byte>()
        var listaFragmenata = mutableListOf<ByteArray>()
        //pp je da je sve kompresovano
        var prvaPoruka = true
        var kompresovano = false;


        var brojac = 0
        val inflater = Inflater(true)
        while (true) {
            val prviIntIBajt = ucitajteIntIBajt()
            izlaz.write(prviIntIBajt.ucitaniInt)
            val prviBajt = prviIntIBajt.ucitaniBajt
            //ovo da sredim malo
            val prviInt = prviIntIBajt.ucitaniInt
            val fin = daLiJeBajtPostavljen(
                prviInt,
                128
            )//prviInt and 128)==128//(prviBajt and 128.toByte() ) == 128.toByte()
            val rsv2 = daLiJeBajtPostavljen(prviInt, 32)//prviBajt and 32.toByte() )== 32.toByte()
            val rsv3 = daLiJeBajtPostavljen(prviInt, 16)//(prviBajt and 16.toByte() )== 16.toByte()
            val rsv1 = daLiJeBajtPostavljen(prviInt, 64)//prviBajt and 64.toByte() )== 64.toByte()
            val opkod = prviInt and 15//prviBajt and 15.toByte()

            val drugiIntIBajt = ucitajteIntIBajt()
            izlaz.write(drugiIntIBajt.ucitaniInt)
            val drugiBajt = drugiIntIBajt.ucitaniBajt
            val drugiInt = drugiIntIBajt.ucitaniInt
            val mask = daLiJeBajtPostavljen(drugiInt, 128)//drugiBajt and 128.toByte() )== 128.toByte()

            val duzinaPrviDeo = drugiInt and 127 //drugiBajt and 127.toByte()
            println("Duzina prvi deo ${duzinaPrviDeo}")
            val duzina = when (duzinaPrviDeo) {
                126 -> ucitajteIPrepisiteDodatneBajtoveZaDuzinu(2)
                127 -> ucitajteIPrepisiteDodatneBajtoveZaDuzinu(4)
                else -> duzinaPrviDeo//.toInt()//(duzinaPrviDeo and 127.toByte()).toInt()
            }
            val maskiranje = ByteArray(4)
            if (mask) {
                //maskiranje = ucitajteIPrepisiteDodatneBajtoveZaDuzinu(4)//ali to ne treba teka realno
                for (i in 0..3) {
                    val ucitaniIntIBajt = ucitajteIntIBajt()
                    izlaz.write(ucitaniIntIBajt.ucitaniInt)
                    maskiranje[i] = ucitaniIntIBajt.ucitaniBajt
                }
            }


            val bajtovi = ByteArray(duzina)
            for (i in 0..duzina - 1) {
                val ucitaniIntIBajt = ucitajteIntIBajt()
                izlaz.write(ucitaniIntIBajt.ucitaniInt)
                bajtovi[i] = ucitaniIntIBajt.ucitaniBajt

            }

            // ovaj deo koda treba odatle da maknem da ne smeta jer je tu nepotreban, neka ga nbeka druga nit demaskira i neka ga ona posle obradi


            //ah ajde samo na brzinu da ide

            if (mask) {
                println("maskiranje je ${String(maskiranje)}")
                for (i in 0..bajtovi.size - 1) {
                    // println("maskiranje ${bajtovi[i] xor maskiranje[i % 4]} = ${bajtovi[i]} xor ${maskiranje[i % 4]} ")
                    bajtovi[i] = bajtovi[i] xor maskiranje[i % 4]
                }
            }

            listaBajtovaIzPoruke.addAll(bajtovi.toList())
            listaFragmenata.add(bajtovi)
            if (prvaPoruka) {
                if (rsv1) {
                    kompresovano = true

                }
                prvaPoruka = false
            }

            if (fin) {
                prvaPoruka = true
                if (kompresovano) {
                    kompresovano = false
                    listaBajtovaIzPoruke.addAll(byteArrayOf(0, 0, 255.toByte(), 255.toByte()).toList())
                    val dekompresovaniBajtovi = mutableListOf<Byte>()
                    try {

                        inflater.setInput(listaBajtovaIzPoruke.toByteArray())
                        val velicinaNizaZaDekodovanje = 100
                        var brojDekodovanih = velicinaNizaZaDekodovanje
                        while (brojDekodovanih == velicinaNizaZaDekodovanje) {

                            val zaDekodovanje = ByteArray(velicinaNizaZaDekodovanje)
                            brojDekodovanih = inflater.inflate(zaDekodovanje)
                            for (i in 0..brojDekodovanih - 1) {
                                dekompresovaniBajtovi.add(zaDekodovanje[i])
                            }

                        }
                    } catch (e: Exception) {

                        println("Opet dekompresija greska: ${e.stackTraceToString()}")
                        println("Nece da se dekompresuje: PRvi int je ${prviInt} Opkod: ${opkod} Brojac: ${brojac}  Salje: ${koSalje} Mask: ${mask} Fin: ${fin}  RSV1: ${rsv1} ${listaBajtovaIzPoruke.map { it.toInt() }}\n a fragmenti su ${listaFragmenata.size} ${listaFragmenata.map { it.toList() }} ")

                    }
                    println(
                        """
                        
                           Prvi int je ${prviInt} Brojac: ${brojac} Fin: ${fin} Opkod: ${opkod}  RSV1: ${rsv1} Salje: ${koSalje}
                         Dekompresovano: Duzina: ${dekompresovaniBajtovi.size} ${String(dekompresovaniBajtovi.toByteArray())} kraj dekompresovanog
                         Kompresovano: ${duzina}  ${String(listaBajtovaIzPoruke.toByteArray())}\n a fragmenti  kompresovanog su ${listaFragmenata.size} ${listaFragmenata.map { it.toList() }} kraj kompresovanog
                         
                         
                         """.trimIndent()
                    )
                    Komunikacija.kanalZaKomunikacijuWebSoket.send(
                        WebSoketPoruka(
                            url,
                            saljeKlijent,
                            String(dekompresovaniBajtovi.toByteArray())
                        )
                    )

                } else {
                    println("Cela poruka je websocket: ${String(listaBajtovaIzPoruke.toByteArray())}")
                    Komunikacija.kanalZaKomunikacijuWebSoket.send(
                        WebSoketPoruka(
                            url,
                            saljeKlijent,
                            String(listaBajtovaIzPoruke.toByteArray())
                        )
                    )

                }

                listaBajtovaIzPoruke = mutableListOf()
                listaFragmenata = mutableListOf()
            }


            //println("Salje: ${koSalje} url je ${url} Fin je ${fin} opkod je ${opkod} mask je ${mask} rsv1 je ${rsv1} duzina je ${bajtovi.size} a bajtovi su : ${String(bajtovi)}")

            brojac++
        }

    }

}

