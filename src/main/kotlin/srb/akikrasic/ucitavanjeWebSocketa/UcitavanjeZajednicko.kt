package srb.akikrasic.ucitavanjeWebSocketa

import srb.akikrasic.ucitavanjezahtevaiodgovora.UcitaniIntIBajt
import srb.akikrasic.ucitavanjezahtevaiodgovora.ucitaniIntIBajtMinus1
import java.io.InputStream
import java.io.OutputStream

open class UcitavanjeZajednicko(open val inp: InputStream, open val out: OutputStream) {
    suspend fun ucitajteIntIBajt(): UcitaniIntIBajt {
        try {
            val ucitaniInt = inp.read()
            return UcitaniIntIBajt(ucitaniInt)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Doslo je do greske prilikom ucitavanja")
            return ucitaniIntIBajtMinus1
        }

    }
}