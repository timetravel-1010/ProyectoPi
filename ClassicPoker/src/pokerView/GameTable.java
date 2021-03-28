/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */

package pokerView;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * JPanel que representa la mesa de juego
 */
public class GameTable extends JPanel {
    private List<JManoPanel> playersView;
    private JLabel textBig, textSmall;

    /**
     * Instantiates a new GameTable.
     */
    public GameTable(List<JManoPanel> playersView, JLabel textBig, JLabel textSmall) {
        this.setLayout(new BorderLayout());
        this.playersView = playersView;
        this.textBig = textBig;
        this.textSmall = textSmall;
        this.setOpaque(false);
        initGUI();
    }

    /**
     * Inits the GUI
     */
    private void initGUI() {
        JPanel rowPane = new JPanel(new FlowLayout());
        JPanel colPane = new JPanel();
        rowPane.setOpaque(false);
        colPane.setOpaque(false);

        rowPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        rowPane.add(playersView.get(1));
        rowPane.add(Box.createRigidArea(new Dimension(50, 0)));
        rowPane.add(playersView.get(2));

        add(rowPane, BorderLayout.PAGE_START);

        // #--------------------------------

        rowPane = new JPanel(new FlowLayout());
        rowPane.setOpaque(false);

        rowPane.add(playersView.get(0));

        // textSmall.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        colPane.setLayout(new BoxLayout(colPane, BoxLayout.PAGE_AXIS));

        setJLabelSize(textBig, new Dimension(350, 40));
        textBig.setHorizontalAlignment(SwingConstants.CENTER);
        textBig.setFont(Resources.RobotoBold.deriveFont(22f));
        textBig.setForeground(Color.WHITE);

        setJLabelSize(textSmall, new Dimension(350, 40));
        textSmall.setHorizontalAlignment(SwingConstants.CENTER);
        textSmall.setFont(Resources.RobotoBold.deriveFont(16f));
        textSmall.setForeground(Color.WHITE);

        colPane.add(textBig);
        colPane.add(textSmall);
        rowPane.add(colPane);

        rowPane.add(playersView.get(3));

        add(rowPane, BorderLayout.CENTER);

        // #--------------------------------

        rowPane = new JPanel(new FlowLayout());
        rowPane.setOpaque(false);
        rowPane.add(playersView.get(4));
        add(rowPane, BorderLayout.PAGE_END);
    }

    /**
     * Fija el tamaño para el JLabel dado
     * @param obj el JLabel
     * @param size
     */
    private void setJLabelSize(JLabel obj, Dimension size) {
        obj.setPreferredSize(size);
        obj.setMinimumSize(size);
        obj.setMaximumSize(size);
    }
}
