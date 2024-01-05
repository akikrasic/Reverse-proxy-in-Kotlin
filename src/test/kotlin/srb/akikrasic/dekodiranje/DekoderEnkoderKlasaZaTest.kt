package srb.akikrasic.dekodiranje

import org.junit.jupiter.api.Assertions.assertEquals

abstract class DekoderEnkoderKlasaZaTest {

    abstract fun enkodujteString(s:String ):ByteArray


    fun enkodavanjeDekodovanje(dekoder:Dekoder){
        val s = "kompir"
        val enkodovaniNizBajtova = enkodujteString(s)
        println("Enkodovani string: ${String(enkodovaniNizBajtova)}")
        val dekodovaniString = dekoder.dekodujteUString(enkodovaniNizBajtova)
        assertEquals(s, dekodovaniString)
    }
}