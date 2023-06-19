package srb.akikrasic

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Proba{
    val podatak = "kompir"
    suspend fun metoda(i:Int) {
        delay(3000)
        println("radi metoda ${podatak} ${i}")
    }
}


fun main()= runBlocking{

    val p = Proba()
    var j =GlobalScope.async {  }
    for (i in 0..4){
        println(i)
          j= GlobalScope.async {
            p.metoda(i)
        }

    }

    j.await()
}