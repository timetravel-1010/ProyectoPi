/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package clientebj;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class PrincipalClienteBJ {
	/**
     * The main method.
     * @param args the arguments.
	 */
	public static void main(String[] args) {

		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				BlackJackView player = new BlackJackView();
			}
		});
	}
}
