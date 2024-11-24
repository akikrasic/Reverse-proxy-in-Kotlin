package srb.akikrasic.forma.modelitabele

import srb.akikrasic.podaci.HederIVrednost
import javax.swing.table.AbstractTableModel



class ModelTabeleHederi :AbstractTableModel(){
    val lista = mutableListOf(HederIVrednost(),HederIVrednost(),HederIVrednost())
    override fun getRowCount(): Int  = lista.size

    override fun getColumnCount(): Int =2

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val h = lista[rowIndex]
        if( columnIndex==0){
            return h.headerNaziv
        }
        return h.headerVrednost
    }
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean  = true

    override fun getColumnName(column: Int): String  = ModelTabeleNaziviKolona.naziviKolonaTabelaHederi[column]

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        val h = lista[rowIndex]
        if (columnIndex == 0) {
            h.headerNaziv = aValue as String
        } else {
            h.headerVrednost = aValue as String
        }
        fireTableDataChanged()
        if( rowIndex == lista.size - 1 ) {
            lista.add(HederIVrednost())
        }
    }
}