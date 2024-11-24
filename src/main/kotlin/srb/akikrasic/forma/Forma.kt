package srb.akikrasic.forma

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import srb.akikrasic.forma.paneli.HttpKomunikacijaPanel
import srb.akikrasic.forma.paneli.MojeSlanjeZahtevaPanel
import srb.akikrasic.forma.paneli.PrenosInformacijaZaPretragu
import srb.akikrasic.forma.paneli.WSPanel
import srb.akikrasic.komunikacija.Komunikacija
import srb.akikrasic.komunikacija.KomunikacijaPodaci
import srb.akikrasic.ucitavanjeWebSocketa.WebSoketPoruka
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev
import java.awt.GridBagLayout
import java.util.concurrent.Executors
import javax.swing.*

class Forma : JFrame() {

    var httpKomunikacijaPanel = HttpKomunikacijaPanel(this)

    var wsPanel = WSPanel()

    fun osnovneOperacije() {
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val velicinaEkrana = toolkit.screenSize
        setSize(velicinaEkrana.width, velicinaEkrana.height)
        isVisible = true
    }

    init {
        GlobalScope.launch(Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()) {
            while (true) {
                dodajteUFormu(Komunikacija.kanalZaKomunikaciju.receive())
            }
        }
        GlobalScope.launch(Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()) {
            while (true) {
                dodajteUFormuWebSoket(Komunikacija.kanalZaKomunikacijuWebSoket.receive())
            }
        }
        osnovneOperacije()
        val layout = GridBagLayout()
        this.contentPane.layout = layout
        this.contentPane = httpKomunikacijaPanel

        val bar = JMenuBar()
        val glavniMeni = JMenu("Мени")
        val httpMeni = JMenuItem("Хттп саобраћај")
        val wsMeni = JMenuItem("Веб сокит")
        val mojeSlanjeZahteva = JMenuItem("Моје слање захтева")
        glavniMeni.add(httpMeni)
        glavniMeni.add(wsMeni)
        glavniMeni.add(mojeSlanjeZahteva)
        bar.add(glavniMeni)
        httpMeni.addActionListener{
            httpKomunikacijaPanel = HttpKomunikacijaPanel(this)
            this.contentPane = httpKomunikacijaPanel
            osveziteFormu()
        }
        wsMeni.addActionListener{
            wsPanel = WSPanel()
            this.contentPane = wsPanel

            osveziteFormu()
        }
        mojeSlanjeZahteva.addActionListener{
            this.contentPane = MojeSlanjeZahtevaPanel()
            osveziteFormu()
        }
        this.jMenuBar = bar

    }
    fun osveziteFormu(){
        this.revalidate()
        this.repaint()
    }

    fun dodajteUFormu(k: KomunikacijaPodaci) {
        httpKomunikacijaPanel.dodajteUFormu(k)
    }


    fun pretraga(zaPretragu: PrenosInformacijaZaPretragu) {
        httpKomunikacijaPanel.pretraga(zaPretragu)

    }

    suspend fun dodajteUFormuWebSoket(wsPoruka:WebSoketPoruka){
        wsPanel.dodajteUFormu(wsPoruka)
    }

    fun ucitajteZahtevUMojeSlanjeZahtevaPanel(host:String, zahtev:Zahtev){
        this.contentPane = MojeSlanjeZahtevaPanel(host, zahtev)
        osveziteFormu()
    }



}