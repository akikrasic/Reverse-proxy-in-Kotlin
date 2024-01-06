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
            1->listaSvih[rowIndex].zahtev.url
            2->listaSvih[rowIndex].zahtev.metoda
            else->""
        }
    fun dodajte( k:KomunikacijaPodaci){
        listaSvih.add(k)
    }
    fun napraviteStringZaPrikazUTextAreiOdgovor(indeks:Int):String = pravljenjeStringaZaPrikazUTekstArei.napraviteTekstOdOdgovora(listaSvih[indeks])
    fun napraviteStringZaPrikazUTextAreiZahtev(indeks:Int):String = pravljenjeStringaZaPrikazUTekstArei.napraviteTekstOdZahteva(listaSvih[indeks])

}