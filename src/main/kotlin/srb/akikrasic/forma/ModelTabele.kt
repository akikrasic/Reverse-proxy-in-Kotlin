package srb.akikrasic.forma

import srb.akikrasic.komunikacija.KomunikacijaPodaci
import javax.swing.table.AbstractTableModel

class ModelTabele:AbstractTableModel() {
    val kolone = arrayOf("Host", "URL", "Metoda")
    val listaSvih = mutableListOf<KomunikacijaPodaci>()
    val pravljenjeStringaZaPrikazUTekstArei = PravljenjeStringaZaPrikazUTekstArei()
    override fun getRowCount(): Int  = listaSvih.size

    override fun getColumnCount(): Int = kolone.size

    override fun getColumnName(column: Int): String  = kolone[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any   =
        when(columnIndex){

            0->listaSvih[rowIndex].host
            1->listaSvih[rowIndex].url
            2->listaSvih[rowIndex].metoda
            else->""
        }
    fun dodajte( k:KomunikacijaPodaci){
        listaSvih.add(k)
    }
    fun napraviteStringZaPrikazUTextArei(indeks:Int):String = pravljenjeStringaZaPrikazUTekstArei.napraviteTekstOdKomunikacije(listaSvih[indeks])
}