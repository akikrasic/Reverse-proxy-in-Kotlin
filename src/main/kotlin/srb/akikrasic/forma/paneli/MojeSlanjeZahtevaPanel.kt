package srb.akikrasic.forma.paneli

import srb.akikrasic.forma.modelitabele.ModelTabeleHederi
import srb.akikrasic.korisno.GuiKorisno
import srb.akikrasic.ucitavanjezahtevaiodgovora.Zahtev
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javax.swing.*



class MojeSlanjeZahtevaPanel(val zahtev: Zahtev = Zahtev()) : JPanel(){


    val metodaKombo = JComboBox<String>()
    val urlPolje = JTextField()
    val tabelaHederi = JTable()
    val zahtevTeloArea = JTextArea()
    val odgovorSveArea = JTextArea()
    val dugmePosaljite = JButton("Пошаљите")

    val modelTabeleHederi = ModelTabeleHederi()

    val mapaBilderi = mutableMapOf<String, ()->HttpRequest.Builder> ("GET" to this::get, "POST" to this::post,
        "PUT" to this::put, "DELETE" to this::delete)

    fun get(): HttpRequest.Builder = HttpRequest.newBuilder().GET()
    fun post(): HttpRequest.Builder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(zahtevTeloArea.text.trim()))
    fun put(): HttpRequest.Builder = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(zahtevTeloArea.text.trim()))
    fun delete(): HttpRequest.Builder = HttpRequest.newBuilder().DELETE()


    init{
        listOf("GET", "POST", "PUT", "DELETE").forEach {
            metodaKombo.addItem(it)
        }
        urlPolje.text = "https://"
        layout = GridBagLayout()
        val c = GuiKorisno.napraviteGridBagConstraints()

        c.weightx = 1.0
        c.weighty =0.05

        val panelGore = napravitePanelGore()
        add(panelGore, c)


        c.gridx = 0
        c.gridy = 1
        c.weightx = 1.0
        c.weighty = 0.05
        c.gridheight = 1
        add(JScrollPane(tabelaHederi), c)
        c.gridy = 2
        c.weighty =0.9
        add(JScrollPane(zahtevTeloArea), c)

        c.gridx = 1
        c.gridy = 0
        c.weighty = 1.0
        c.gridheight = 3
        add(JScrollPane(odgovorSveArea), c)

        dugmePosaljite.addActionListener {
            posaljite()
        }
        tabelaHederi.model = modelTabeleHederi
        tabelaHederi.addMouseListener(object:MouseListener{
            override fun mouseClicked(e: MouseEvent?) {

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
        tabelaHederi.addKeyListener( object: KeyListener {
            override fun keyTyped(e: KeyEvent?) {
                println("radi")
                println(tabelaHederi.selectedRow)
            }

            override fun keyPressed(e: KeyEvent?) {

            }

            override fun keyReleased(e: KeyEvent?) {

            }

        })
    }

    fun napravitePanelGore():JPanel{
        val panelGore = JPanel()
        panelGore.layout= GridBagLayout()

        val ogranicenjaZaPanel = GuiKorisno.napraviteGridBagConstraints()
        ogranicenjaZaPanel.fill = GridBagConstraints.HORIZONTAL
        ogranicenjaZaPanel.weightx = 0.3
        panelGore.add(metodaKombo, ogranicenjaZaPanel)
        ogranicenjaZaPanel.gridx =1
        ogranicenjaZaPanel.weightx = 0.7
        panelGore.add(urlPolje,ogranicenjaZaPanel)

        ogranicenjaZaPanel.gridy = 1
        ogranicenjaZaPanel.fill = GridBagConstraints.NONE
        panelGore.add(dugmePosaljite, ogranicenjaZaPanel)
        return panelGore
    }
    fun posaljite(){
        val client = HttpClient.newHttpClient()
        val request = mapaBilderi[metodaKombo.selectedItem]!!().uri(URI.create(urlPolje.text.trim())).build()
        val resp = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        odgovorSveArea.text = resp

    }



}