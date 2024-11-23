package srb.akikrasic.forma.modelitabele

import srb.akikrasic.podaci.Podaci
import srb.akikrasic.ucitavanjeWebSocketa.WebSoketPoruka
import javax.swing.table.AbstractTableModel

class ModelTabeleWebSocketPoruke(val lista:List<WebSoketPoruka>) : AbstractTableModel() {


    override fun getRowCount(): Int  = lista.size

    override fun getColumnCount(): Int  = 2

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any  =if (columnIndex==0){
        if(lista[rowIndex].klijent){"Клијент"}else{"Сервер"}}else{ lista[rowIndex].poruka}

}