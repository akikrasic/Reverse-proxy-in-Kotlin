package srb.akikrasic.forma.paneli

import srb.akikrasic.korisno.GuiKorisno
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class MojeSlanjeZahtevaPanel : JPanel(){

    val metodaKombo = JComboBox<String>()
    val urlPolje = JTextField()
    val tabelaHederi = JTable()
    val zahtevTeloArea = JTextArea()
    val odgovorSveArea = JTextArea()

    init{
        layout = GridBagLayout()
        val c = GuiKorisno.napraviteGridBagConstraints()
        c.weightx = 0.2
        c.weighty = 0.2
        add(metodaKombo, c)
        c.gridx = 1
        c.weightx = 0.8
        add(urlPolje, c)
        c.gridx = 0
        c.gridy = 1
        c.weightx = 1.0
        c.weighty = 0.3
        add(tabelaHederi, c)
        c.gridy = 2
        add(zahtevTeloArea, c)

    }



}