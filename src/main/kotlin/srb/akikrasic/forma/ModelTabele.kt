package srb.akikrasic.forma

import srb.akikrasic.komunikacija.KomunikacijaPodaci
import javax.swing.table.AbstractTableModel

class ModelTabele:AbstractTableModel() {
    val kolone = arrayOf("Host", "URL", "Metoda")
    val pravljenjeStringaZaPrikazUTekstArei = PravljenjeStringaZaPrikazUTekstArei()
    val radSaListomUModeluTabele = RadSaListomUModeluTabele()
    override fun getRowCount(): Int  = radSaListomUModeluTabele.listaZaPrikaz.size

    override fun getColumnCount(): Int = kolone.size

    override fun getColumnName(column: Int): String  = kolone[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any   =
        when(columnIndex){
        
            0->radSaListomUModeluTabele.listaZaPrikaz[rowIndex].host
            1->radSaListomUModeluTabele.listaZaPrikaz[rowIndex].zahtev.url
            2->radSaListomUModeluTabele.listaZaPrikaz[rowIndex].zahtev.metoda
            else->""
        }
    suspend fun dodajte(k:KomunikacijaPodaci){
        radSaListomUModeluTabele.dodajteUListu(k)
    }
    fun napraviteStringZaPrikazUTextAreiOdgovor(indeks:Int):String = pravljenjeStringaZaPrikazUTekstArei.napraviteTekstOdOdgovora(radSaListomUModeluTabele.listaZaPrikaz[indeks])
    fun napraviteStringZaPrikazUTextAreiZahtev(indeks:Int):String = pravljenjeStringaZaPrikazUTekstArei.napraviteTekstOdZahteva(radSaListomUModeluTabele.listaZaPrikaz[indeks])

    suspend fun pretraga(zaPretragu:String){
        radSaListomUModeluTabele.pretraga(zaPretragu)
    }
}