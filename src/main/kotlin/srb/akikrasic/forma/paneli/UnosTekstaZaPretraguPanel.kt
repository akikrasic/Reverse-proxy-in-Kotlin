package srb.akikrasic.forma.paneli

import srb.akikrasic.forma.Forma
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class UnosTekstaZaPretraguPanel(val forma: Forma) : JPanel() {

    val labela = JLabel("Претрага")
    val polje = JTextField()

    init{
        layout = GridBagLayout()
        val c = GridBagConstraints()
        c.gridx=0
        c.gridy = 0
        c.weightx=1.0
        c.fill = GridBagConstraints.BOTH
        c.insets = Insets(5,5,5,5)
        c.anchor = GridBagConstraints.EAST
        add(labela,c)
        c.gridy = 1
        add(polje,c)

    }
}