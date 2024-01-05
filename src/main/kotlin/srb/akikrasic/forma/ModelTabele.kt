package srb.akikrasic.forma

import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.ucitavanjezahtevaiodgovora.ZajednickoZaZahtevIOdgovor
import javax.swing.table.AbstractTableModel

class ModelTabele:AbstractTableModel() {
    val kolone = arrayOf("Host", "URL", "Metoda")
    val lista = mutableListOf<KomunikacijaPodaci>()
    override fun getRowCount(): Int  = lista.size

    override fun getColumnCount(): Int = kolone.size

    override fun getColumnName(column: Int): String  = kolone[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any   =
        when(columnIndex){

            0->lista[rowIndex].host
            1->lista[rowIndex].url
            2->lista[rowIndex].metoda
            else->""
        }
    fun dodajte( k:KomunikacijaPodaci){
        lista.add(k)
    }
}