package srb.akikrasic.dekodiranje

import com.nixxcode.jvmbrotli.common.BrotliLoader
import com.nixxcode.jvmbrotli.dec.Decoder
import com.nixxcode.jvmbrotli.enc.Encoder
open class A{
    open fun metoda(){
        println("A")
    }
}
class B:A(){
    override fun metoda(){
        println("B")
    }
}

open class C{
    open val a = A()
}
open class D:C(){
    fun metoda2(){
        println("deruga")
    }
     override val a = B()
}
fun main(){
    println(BrotliLoader.isBrotliAvailable())
val enkr = Encoder.compress(" Космет Србија гсдфгг г дфгфгдс фгфд гдфгдф г г дсфгдсф дг дфсгдсфг дфсг дфгдфсг дфгдг дфг дфг дсфг дфг фг дгсфг дсг сдг сдфг сдг сдфг сдфг дсфг ".toByteArray())
    val dekr = Decoder.decompress(enkr)
    println(String(dekr))
println(String(enkr))
println("radi novi alat za hak")
val c = C()
    c.a.metoda()
    val d = D()
    d.a.metoda()
    d.metoda2()
}