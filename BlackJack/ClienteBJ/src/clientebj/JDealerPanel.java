/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package clientebj;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;

/**
 * Clase utilizada para mostrar el estado del juego del Dealer.
 */
public class JDealerPanel extends JManoPanel {

    /**
     * Inits the GUI.
     */
    protected void initGUI() {
        this.setBackground(null);
        Resources.setJPanelSize(this, new Dimension(350, 330)); //335
        this.setOpaque(false);

        icono = new JLabel(Resources.getImage("dealer.png"));
        add(icono);

        add(Box.createRigidArea(new Dimension(120, 0)));

        cartas = new JCartasPanel(JCartasSize);
        add(cartas);
    }
}
