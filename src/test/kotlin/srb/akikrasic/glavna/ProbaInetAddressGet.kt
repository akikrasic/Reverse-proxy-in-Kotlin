package srb.akikrasic.glavna

import org.junit.jupiter.api.Test
import java.net.InetAddress

class ProbaInetAddressGet {

    @Test
    fun proba(){
       val x = InetAddress.getByName("static.xx.fbcdn.net")
       println(x)
    }

}