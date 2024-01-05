package srb.akikrasic.forma

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import srb.akikrasic.komunikacija.Komunikacija
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.table.DefaultTableModel

class Forma :JFrame() {

    val polje =  JTextField()
    val tabela = JTable()
    val area = JTextArea()
    val modelTabele = ModelTabele()
    private fun osnovneOperacije(){
        this.defaultCloseOperation=JFrame.EXIT_ON_CLOSE
        val velicinaEkrana = toolkit.screenSize
        setSize(velicinaEkrana.width, velicinaEkrana.height)
    }

    init{
        tabela.model = modelTabele
        tabela.autoCreateColumnsFromModel = true
        osnovneOperacije()
        isVisible = true
        val layout = GridBagLayout()
        this.contentPane.layout = layout
        val c = GridBagConstraints()
        c.gridx = 0
        c.gridy = 0
        c.weighty = 0.05
        c.weightx=0.5
        c.insets = Insets(10,10,10,10)
        c.fill =GridBagConstraints.BOTH
        contentPane.add(polje, c)
        c.gridy = 1
        c.weighty= 0.95
        c.weightx = 0.5
        c.fill = GridBagConstraints.BOTH
        contentPane.add(JScrollPane(tabela), c)

        c.gridy = 1
        c.gridx = 1
        c.weighty= 0.95
        c.weightx = 0.5
        c.fill = GridBagConstraints.BOTH
        contentPane.add(JScrollPane(area), c)
        GlobalScope.launch (Dispatchers.Default){
            while(true){
                dodajteUFormu(Komunikacija.kanalZaKomunikaciju.receive())
            }
        }
        tabela.addMouseListener( object: MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                    if ( e!!.clickCount==2){
                        val sb = StringBuilder()
                        val k = modelTabele.lista[tabela.selectedRow]//mora lepse!!!
                        k.hederi.mapaOriginalnihHedera.forEach { k, v-> sb.append(k).append(": ").append(v).append("\n") }
                        sb.append("\n").append(k.poruka)
                        area.text=sb.toString()
                    }
            }

            override fun mousePressed(e: MouseEvent?) {
            }

            override fun mouseReleased(e: MouseEvent?) {
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseExited(e: MouseEvent?) {
            }

        })
    }

    fun dodajteUFormu(k:KomunikacijaPodaci){
        SwingUtilities.invokeLater{
            modelTabele.dodajte(k)
            tabela.model = modelTabele
            modelTabele.fireTableDataChanged()
        }
    }


}