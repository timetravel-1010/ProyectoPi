/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package clientebj;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Clase que agrupa y administra componentes del juego.
 */
public class GameTable extends JPanel {

    private List<JManoPanel> jugadores;
    private JLabel notificaciones;
    private JButton pedirCarta, plantar;

    /**
     * Instantiates a new GameTable.
     * @param jugadores
     * @param notificaciones
     * @param pedirCarta
     * @param plantar
     */
    public GameTable(List<JManoPanel> jugadores, JLabel notificaciones, JButton pedirCarta, JButton plantar) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.jugadores = jugadores;
        this.notificaciones = notificaciones;
        this.pedirCarta = pedirCarta;
        this.plantar = plantar;
        initGUI();
    }

    /**
     * Inits the GUI.
     */
    private void initGUI() {
        JPanel colPane = getJPanelOpaque();
        colPane.setLayout(new BoxLayout(colPane, BoxLayout.PAGE_AXIS));

        //colPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jugadores.get(2).setAlignmentX(Component.LEFT_ALIGNMENT);
        pedirCarta.setAlignmentX(Component.LEFT_ALIGNMENT);

        colPane.add(Box.createRigidArea(new Dimension(10, 20)));
        colPane.add(jugadores.get(2));
        colPane.add(pedirCarta);
        add(colPane, BorderLayout.LINE_START);

        colPane = getJPanelOpaque();
        colPane.setLayout(new BoxLayout(colPane, BoxLayout.PAGE_AXIS));

        notificaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        jugadores.get(1).setAlignmentX(Component.CENTER_ALIGNMENT);

        colPane.add(notificaciones);
        colPane.add(Box.createRigidArea(new Dimension(10, 35)));
        colPane.add(jugadores.get(1));
        add(colPane, BorderLayout.CENTER);

        colPane = getJPanelOpaque();
        colPane.setLayout(new BoxLayout(colPane, BoxLayout.PAGE_AXIS));

        jugadores.get(0).setAlignmentX(Component.RIGHT_ALIGNMENT);
        plantar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        colPane.add(Box.createRigidArea(new Dimension(10, 20)));
        colPane.add(jugadores.get(0));
        colPane.add(plantar);
        add(colPane, BorderLayout.LINE_END);
    }

    /**
     * @return un JPanel no opaco.
     */
    private JPanel getJPanelOpaque() {
        JPanel colPane = new JPanel();
        colPane.setOpaque(false);
        return colPane;
    }
}
