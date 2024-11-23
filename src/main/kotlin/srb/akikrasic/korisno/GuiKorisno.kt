package srb.akikrasic.korisno

import java.awt.GridBagConstraints
import java.awt.Insets

object GuiKorisno {

    fun napraviteGridBagConstraints(): GridBagConstraints {
        val c = GridBagConstraints()
        c.gridx = 0
        c.gridy = 0
        c.insets = Insets(10, 10, 10, 10)
        c.fill = GridBagConstraints.BOTH
        c.anchor = GridBagConstraints.CENTER
        return c
    }

}