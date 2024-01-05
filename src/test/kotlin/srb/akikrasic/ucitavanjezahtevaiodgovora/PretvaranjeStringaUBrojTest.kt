package srb.akikrasic.ucitavanjezahtevaiodgovora

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PretvaranjeStringaUBrojTest{
    @Test
    fun pretvoriteObicanBrojIspravan(){
        assertEquals(123,PretvaranjeStringaUBroj.pretvaranjeStringaUBroj("123"))
    }

    @Test
    fun pretvariteObicanBrojNeispravan(){
        assertEquals(0, PretvaranjeStringaUBroj.pretvaranjeStringaUBroj("123a"))
    }

    @Test
    fun pretvoriteHeksaDecimalniIspravan(){
        assertEquals(171, PretvaranjeStringaUBroj.pretvaranjeHeksadecimalnogStringaUBroj("ab"))
        assertEquals(2748, PretvaranjeStringaUBroj.pretvaranjeHeksadecimalnogStringaUBroj("abc"))
        assertEquals(177, PretvaranjeStringaUBroj.pretvaranjeHeksadecimalnogStringaUBroj("b1"))
    }

    @Test
    fun pretvoriteHeksaDecimalniNeispravan(){
        assertEquals(0, PretvaranjeStringaUBroj.pretvaranjeHeksadecimalnogStringaUBroj("gh2234"))
    }


}