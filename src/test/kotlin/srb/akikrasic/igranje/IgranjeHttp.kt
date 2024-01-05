package srb.akikrasic.igranje

import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class IgranjeHttp {

    @Test
    fun proba(){

        val zahtev = HttpRequest.newBuilder().uri(URI("https://www.facebook.com")).GET().build()
        val odg = HttpClient.newBuilder().build().send(zahtev, HttpResponse.BodyHandlers.ofString())
        println(odg.body())
        println("${odg.version()} ${odg.statusCode()}")
    }
}