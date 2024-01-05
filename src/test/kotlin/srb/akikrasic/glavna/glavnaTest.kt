package srb.akikrasic.glavna

import napraviteOdgovor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class glavnaTest{


    @Test
    fun napraviteOdgovorTest(){
        val verzija = "HTTP/1.1"
        val status = "200"
        val poruka = "OK"
        val contentEncodingKljuc ="Content-Encoding"
        val contentEncodingVrednost = "br"

        val contentLengthKljuc ="Content-Length"
        val contentLengthVrednost = "123"

        val acceptKljuc ="Accept"
        val acceptVrednost = " kompir; 123"
        val bajtoviString = "bajtovi zmijete"

        val s ="$verzija $status $poruka\r\n${contentEncodingKljuc}:${contentEncodingVrednost}\r\n$contentLengthKljuc:$contentLengthVrednost\r\n$acceptKljuc:$acceptVrednost\r\n\r\n$bajtoviString"
        val odgovor = napraviteOdgovor(s.toByteArray())
        assertEquals(verzija, odgovor.verzija )
        assertEquals( status, odgovor.status )
        assertEquals(poruka, odgovor.poruka)
        assertEquals(contentEncodingVrednost, odgovor.headeri[contentEncodingKljuc])
        assertEquals(contentLengthVrednost, odgovor.headeri[contentLengthKljuc])
        assertEquals(acceptVrednost, odgovor.headeri[acceptKljuc])
        assertEquals(bajtoviString, String(odgovor.telo))

    }



}