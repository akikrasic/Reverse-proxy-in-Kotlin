package srb.akikrasic.forma.paneli

import kotlinx.coroutines.sync.Mutex
import srb.akikrasic.forma.modelitabele.ModelTabeleWebSocketPoruke
import srb.akikrasic.forma.modelliste.ModelListeWebSoket
import srb.akikrasic.podaci.Podaci
import srb.akikrasic.podaci.UrlSaWebSoketPorukama
import srb.akikrasic.ucitavanjeWebSocketa.WebSoketPoruka
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*

class WSPanel : JPanel() {

    val listaKonekcija = JList<String>()
    val tabelaPoruka = JTable()
    val area = JTextArea()
    val modelListeWebSoket = ModelListeWebSoket()
    var modelTabeleWebSoketPoruke = ModelTabeleWebSocketPoruke(listOf())

    var trenutnoOdabraniUrl = ""

    val mutex = Mutex()

    init {
        layout = GridBagLayout()
        this.layout = GridBagLayout()

        tabelaPoruka.autoCreateColumnsFromModel = true
        val c = GridBagConstraints()
        c.fill = GridBagConstraints.BOTH
        c.anchor = GridBagConstraints.CENTER
        c.insets = Insets(10, 10, 10, 10)
        c.gridx = 0
        c.gridy = 0
        c.weighty = 1.0
        c.weightx = 0.25
        this.add(JScrollPane(listaKonekcija), c)
        c.gridx = 1
        this.add(JScrollPane(tabelaPoruka), c)
        c.weightx = 0.5
        c.gridx = 2
        this.add(JScrollPane(area), c)


        listaKonekcija.model = modelListeWebSoket
        listaKonekcija.addMouseListener(object: MouseListener{
            override fun mouseClicked(e: MouseEvent?) {

                trenutnoOdabraniUrl = listaKonekcija.selectedValue

                modelTabeleWebSoketPoruke = ModelTabeleWebSocketPoruke(Podaci.mapaSvihUrlovaZaWebSoket[trenutnoOdabraniUrl]?.listaPoruka?:listOf())
                tabelaPoruka.model = modelTabeleWebSoketPoruke
                modelTabeleWebSoketPoruke.fireTableDataChanged()

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

        tabelaPoruka.addMouseListener(object: MouseListener{
            override fun mouseClicked(e: MouseEvent?) {
                val red = tabelaPoruka.selectedRow
                area.text = modelTabeleWebSoketPoruke.lista[red].poruka
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



    suspend fun dodajteUFormu(wsPoruka: WebSoketPoruka) {


        val url = wsPoruka.url

        val urlIPoruke =
            if (url in Podaci.mapaSvihUrlovaZaWebSoket) {
                Podaci.mapaSvihUrlovaZaWebSoket[url]
            } else {
                val urliPorukeUnutrasnji = UrlSaWebSoketPorukama(url)
                Podaci.mapaSvihUrlovaZaWebSoket[url] = urliPorukeUnutrasnji
                dodajteUrlUListuISelektujtePrethodnoSelektovani(url)
                urliPorukeUnutrasnji
            }
        urlIPoruke?.listaPoruka?.add(wsPoruka)

        if( trenutnoOdabraniUrl == url ){
            SwingUtilities.invokeLater {
                val selektovani = tabelaPoruka.selectedRow
                tabelaPoruka.model =
                    ModelTabeleWebSocketPoruke(Podaci.mapaSvihUrlovaZaWebSoket[url]?.listaPoruka ?: listOf())
                modelTabeleWebSoketPoruke.fireTableDataChanged()

                tabelaPoruka.setRowSelectionInterval(selektovani, selektovani)
            }
        }



    }

    suspend fun dodajteUrlUListuISelektujtePrethodnoSelektovani( url:String){
        mutex.lock()
        val selektovaniIndeks = listaKonekcija.selectedIndex

        Podaci.listaSvihUrlovaZaWebSoket.add(url)
        SwingUtilities.invokeLater {
            listaKonekcija.clearSelection()
            listaKonekcija.model = ModelListeWebSoket()

            listaKonekcija.setSelectionInterval(selektovaniIndeks, selektovaniIndeks)
            listaKonekcija.updateUI()
        }
        mutex.unlock()


    }
}