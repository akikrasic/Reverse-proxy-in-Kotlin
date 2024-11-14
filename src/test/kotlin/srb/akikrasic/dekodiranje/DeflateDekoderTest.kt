package srb.akikrasic.dekodiranje

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

class DeflateDekoderTest:DekoderEnkoderKlasaZaTest(){

    val dekoder = DeflateDekoder()
    @Test
    fun dekodovanje(){
//        enkodavanjeDekodovanje(dekoder)

        val s = "kompir dfkjgdf  kg j jgdfg jgkljdfgkdfjg dfgjkdfg jdfklgjdfklgj dfklgjdf kgjfd glkjdfgkljdfgkjdf gkjfdgkldfjgkldfjgk fdjkfjg kldfjg fkdgj fdkgj fdklgjf dkgjfdkgjfd klgjfdkglfdjgk "
        val s1 ="drugi strin koji je veliki za test asfdff sd fsd fsdf sdf sdfsd fsdf sdfsd fsdf sdf sdf sdfadf kljsadf jfdkj sdfj sdfj kdfj kdjf kjdf jsf dkjqdkjf kdfj dksfj ksdfj ksdjf ksdf jksdjfjdf"
        val enk = Deflater()

        enk.setInput(s.toByteArray())
        enk.setInput(s1.toByteArray())
        enk.setInput((s+s1).toByteArray())
        enk.finish()
        val zaUpis = ByteArray(50)
        val listaEnkodovanih = mutableListOf<Byte>()
        while(!enk.finished()){
            val br = enk.deflate(zaUpis)
            println(" Broj izonodenih je ${br} a izonodeno je: ${String(zaUpis)}")
            listaEnkodovanih.addAll(zaUpis.toList())
        }
        val inf = Inflater()
        inf.setInput(listaEnkodovanih.toByteArray())
        val dekodovani = mutableListOf<Byte>()
        while( !inf.finished() ){
            val br = inf.inflate(zaUpis)
            println(" Broj dekodovanih je ${br} a izonodeno je: ${String(zaUpis)}")
            dekodovani.addAll(zaUpis.toList())
        }
        println("Dekodovano je ${String(dekodovani.toByteArray())}")

    }

    override fun enkodujteString(s: String): ByteArray {
        val enk =  Deflater()
        enk.setInput(s.toByteArray())
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        enk.finish()
        while(!enk.finished()){

            val brojUpisanih  = enk.deflate(buffer)

            println(brojUpisanih)


            bos.write(buffer, 0 , brojUpisanih)
            bos.flush()

        }
        return bos.toByteArray()
    }


    @Test
    fun daProbamDaIzvadim(){

        var s = "92, -110, -49, 10, -126, 64, 16, -121, -83, 75, -17, -31, 19, -40, 110, -82, 94, -73, 66, 42, 16, 58, 4, 93, -124, 101, 93, -115, 4, 83, -37, 63, -87, 65, -41, -98, -69, -39, 45, 8, -68, 46, -77, 51, -13, 125, -65, 65, -17, -71, -73, -8, -2, -72, 123, 51, -72, 27, -104, -17, 44, 78, -30, -10, 39, -80, 5, -41, 60, 7, 75, -103, 37, -56, -4, -78, 107, -59, -107, 57, -8, 8, -111, 37, 94, 98, -68, -118, 8, 65, 97, -116, 3, 18, 64, -63, -123, 87, -75, -111, 37, 19, -80, -86, -50, 126, 97, -128, 34, -82, -76, 101, -122, -69, 45, -104, 48, 82, -75, -46, 9, -37, 109, 122, 74, -41, 57, -57, -3, -2, 92, 31, -101, -19, 97, 72, -109, 68, -99, 30, 67, -120, -51, 51, 79, 25, -91, -50, -80, 26, 27, -63, 58, 46, -7, 77, -3, 123, 66, -92, -86, 106, 27, 120, -104, 106, -73, -46, -127, -45, -108, 48, -44, 66, -58, 16, -95, 30, 59, 72, 31, -67, 62, 0, 0"
        s = "-52, 92, -63, 78, -124, 48, 16, -11, -90, -97, -79, -23, -103, 68, -40, -54, 86, -72, -95, -122, -88, -55, 38, 123, 32, -18, -91, -122, 116, 67, -93, -60, 82, 72, 75, 101, 117, 127, -39, -113, 48, 3, -20, -70, -128, -96, 7, 15, -98, -25, -27, 53, -99, -66, 121, -99, 76, -46, 58, 31, -89, 39, 103, -25, 66, -57, -118, -21, 98, -121, 20, -48, -23, 50, 78, 19, -28, 75, 35, -124, -123, 10, -10, 38, 114, -106, 32, 31, -19, 40, -110, 44, -29, -76, -115, 80, -92, 75, 94, 80, -28, -125, 4, -21, 91, -33, -74, -6, 98, 28, -105, -54, 87, -99, -4, 36, 66, -117, -94, -37, -21, 42, 8, -82, 54, 26, 87, 119, 107, -79, -110, 55, -9, -37, 101, 24, -22, -24, 117, -21, 98, -13, -66, 89, -58, 65, -13, -128, -65, -123, -103, 73, -40, 112, 22, 58, 24, -18, 117, 122, 110, -88, -79, 57, 6, 67, -77, -101, 106, 107, -9, -104, 49, -11, 18, 61, -85, 90, -40, 44, 121, 56, 122, -1, 108, 123, 23, 4, 126, -39, 35, -124, 92, 46, 58, -65, -8, 97, 103, 78, 22, -82, 109, 59, -60, -19, 58, 92, -91, -46, -110, -125, 52, -109, 40, 111, 72, 117, 4, -70, -99, 36, -11, -114, 59, 65, 92, -25, 30, -17, 57, -35, -42, -127, 41, 98, 66, -60, 64, -40, 54, 45, -113, -99, -96, -111, 16, -118, 117, 42, -97, -116, 96, -86, -58, -52, 36, -81, 102, -39, 126, 90, -2, 45, -66, 16, 70, 49, 49, 64, -21, -2, -19, -76, 98, -118, -53, 50, -52, 69, -62, 21, 100, 105, -51, 74, -82, 32, 113, -109, -5, -126, -75, 126, 107, -124, -34, -65, 53, -62, -66, 62, -112, 53, 122, -54, 7, 95, 25, -51, -41, -97, -107, -5, 39, 0, 0, 0, -1, -1"
        s="50, -100, 60, 0, -7, 31, -57, 66, 55, -52, -4, 111, 97, -124, 53, -1, -101, -95, 9, 27, 25, -102, -104, -125, -18, -52, 48, -95, 86, -42, 7, 0, 0, 0, -1, -1"
         //s = "243, 72, 205, 201, 201, 7, 0"
        s = "26, -28, 97, -125, 117, -109, -112, -91, 1, -83, 15, -60, 5, 0, 26, -20, -95, -126, 109, -88, -45, -46, 0, 125, 11, 56, -75, 83, 12, 0, 26, -44, 125, 41, -116, 89, 110, 120, -88, 88, -46, -95, 47, 5, 0"

        val niz = s.split(", ")
        val noviNiz = niz.map{it.toInt().toByte()}
        val inf = Inflater(true)
        val zaDekodovanje = ByteArray(1024)
        inf.setInput(noviNiz.toByteArray())
        var x =100

        while(x>0){
            x = inf.inflate(zaDekodovanje)
            println(x)
            println(zaDekodovanje.toList().map{it.toInt().toChar()})
        }
    }
}
