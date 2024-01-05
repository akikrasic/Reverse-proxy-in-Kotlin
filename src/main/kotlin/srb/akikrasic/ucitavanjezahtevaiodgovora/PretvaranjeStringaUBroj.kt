package srb.akikrasic.ucitavanjezahtevaiodgovora

object PretvaranjeStringaUBroj {

    fun pretvaranjeStringaUBroj(broj:String) : Int{
        try {
            return Integer.valueOf(broj)

        }
        catch(e:Exception){
            return 0
        }
    }

    fun pretvaranjeHeksadecimalnogStringaUBroj(broj:String):Int{
        var konacniBroj = 0
        for ( i in 0..broj.lastIndex){
            val cifra = broj[i]
            var cifraZbir=0
            if( cifra>='0' && cifra<='9'){
                cifraZbir=cifra-'0'
            }
            else{
                if(cifra>='a' && cifra<='f'){
                    cifraZbir = cifra-'a'+10
                }
                else{
                    return 0
                }
            }
            konacniBroj*=16
            konacniBroj+=cifraZbir

        }
        return konacniBroj
    }
}