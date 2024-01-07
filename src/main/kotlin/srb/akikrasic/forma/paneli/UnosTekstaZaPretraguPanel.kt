package srb.akikrasic.forma.paneli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import srb.akikrasic.forma.Forma
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
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
        polje.addKeyListener(object:KeyListener{
            override  fun keyTyped(e: KeyEvent?) {

            }

            override fun keyPressed(e: KeyEvent?) {

            }

            override fun keyReleased(e: KeyEvent?) {
                val zaPretragu = polje.text.trim()
                GlobalScope.launch (Dispatchers.Default){ forma.pretraga(zaPretragu)}
            }

        })
    }
}