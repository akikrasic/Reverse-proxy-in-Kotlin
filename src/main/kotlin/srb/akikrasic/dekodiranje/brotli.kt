package srb.akikrasic.dekodiranje
import org.brotli.dec.BrotliInputStream
import java.io.ByteArrayInputStream

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
println("radi novi alat za hak")
val c = C()
    c.a.metoda()
    val d = D()
    d.a.metoda()
    d.metoda2()
}