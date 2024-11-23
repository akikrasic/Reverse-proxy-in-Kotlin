package srb.akikrasic.forma.paneli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import srb.akikrasic.forma.modelitabele.ModelTabeleNaziviKolona
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class UnosTekstaZaPretraguPanel(val panel: HttpKomunikacijaPanel) : JPanel() {

    val poljeHost = JTextField()
    val poljeUrl = JTextField()
    val poljeMetoda = JTextField()
    init{
        layout = GridBagLayout()
        val c = GridBagConstraints()
        c.gridx=0
        c.gridy = 0
        c.weightx=1.0
        c.fill = GridBagConstraints.BOTH
        c.insets = Insets(5,5,5,5)
        c.anchor = GridBagConstraints.EAST
        val labeleNazivi = ModelTabeleNaziviKolona.naziviKolona
        val polja = listOf(poljeHost, poljeUrl, poljeMetoda)
        for( i in 0..2) {
            add(JLabel(labeleNazivi[i]), c)
            c.gridy++
            add(polja[i], c)
            c.gridy++
        }
        val osluskivac = object:KeyListener{
            override  fun keyTyped(e: KeyEvent?) {

            }

            override fun keyPressed(e: KeyEvent?) {

            }

            override fun keyReleased(e: KeyEvent?) {
                GlobalScope.launch (Dispatchers.Default){ panel.pretraga(PrenosInformacijaZaPretragu(
                    poljeHost.text.trim(),
                    poljeUrl.text.trim(),
                    poljeMetoda.text.trim()
                ))
                println(PrenosInformacijaZaPretragu(
                    poljeHost.text.trim(),
                    poljeUrl.text.trim(),
                    poljeMetoda.text.trim()
                ))
                }
            }

        }

        poljeHost.addKeyListener(osluskivac)
        poljeUrl.addKeyListener(osluskivac)
        poljeMetoda.addKeyListener(osluskivac)
    }
}