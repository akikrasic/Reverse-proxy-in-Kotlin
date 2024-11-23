package srb.akikrasic.forma.modelliste

import srb.akikrasic.podaci.Podaci
import srb.akikrasic.podaci.UrlSaWebSoketPorukama
import javax.swing.AbstractListModel

class ModelListeWebSoket:AbstractListModel<String>() {
    val lista = Podaci.listaSvihUrlovaZaWebSoket
    override fun getSize(): Int  = lista.size

    override fun getElementAt(index: Int): String  = lista[index]

}