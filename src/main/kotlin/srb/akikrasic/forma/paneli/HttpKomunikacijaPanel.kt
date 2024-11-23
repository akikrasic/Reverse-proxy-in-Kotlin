package srb.akikrasic.forma.paneli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import srb.akikrasic.forma.modelitabele.ModelTabele
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.korisno.GuiKorisno
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*

class HttpKomunikacijaPanel() : JPanel() {
    val unosTeksta = UnosTekstaZaPretraguPanel(this)
    val tabela = JTable()
    val areaZahtev = JTextArea()
    val areaOdgovor = JTextArea()
    val tabbedPane = JTabbedPane()
    val modelTabele = ModelTabele()


    init{

        this.layout = GridBagLayout()

        tabbedPane.addTab("Захтев", JScrollPane(areaZahtev))
        tabbedPane.addTab("Одговор", JScrollPane(areaOdgovor))
        tabela.model = modelTabele
        tabela.autoCreateColumnsFromModel = true

        val c = GuiKorisno.napraviteGridBagConstraints()

        c.weighty = 0.05
        c.weightx = 0.3

        this.add(unosTeksta, c)
        c.gridy = 1
        c.weighty = 0.95
        c.weightx = 0.5
        c.fill = GridBagConstraints.BOTH
        this.add(JScrollPane(tabela), c)

        c.gridy = 1
        c.gridx = 1
        c.weighty = 0.95
        c.weightx = 0.5
        c.fill = GridBagConstraints.BOTH
        this.add(tabbedPane, c)

        tabela.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                if (e!!.clickCount == 2) {
                    val red = tabela.selectedRow
                    areaZahtev.text = modelTabele.napraviteStringZaPrikazUTextAreiZahtev(red)
                    areaOdgovor.text = modelTabele.napraviteStringZaPrikazUTextAreiOdgovor(red)
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

    fun dodajteUFormu(k: KomunikacijaPodaci) {
        GlobalScope.launch(Dispatchers.Default) {

            modelTabele.dodajte(k)
            osvezavanjeTabele()

        }
    }

    fun osvezavanjeTabele() {
        val selektovaniIndeks = tabela.selectedRow
        tabela.model = modelTabele
        modelTabele.fireTableDataChanged()
        if (selektovaniIndeks > -1) {
            tabela.setRowSelectionInterval(selektovaniIndeks, selektovaniIndeks)
        }
    }

    fun pretraga(zaPretragu: PrenosInformacijaZaPretragu) {


        GlobalScope.launch(Dispatchers.Default) {

            modelTabele.pretraga(zaPretragu)

            osvezavanjeTabele()

        }

    }
}