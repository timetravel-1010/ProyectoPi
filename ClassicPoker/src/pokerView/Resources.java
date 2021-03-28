/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */

package pokerView;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Clase estatica que almacena los recursos globales
 */
public class Resources {
    public static Font Casino;
    public static Font Lounge;
    public static Font Roboto;
    public static Font RobotoBold;

    /**
     * Cargar la fuente
     * @param src InputStream de la fuente
     */
    public static void loadLounge(Object obj) {
        try {
            Lounge = Font.createFont(Font.TRUETYPE_FONT,
                    obj.getClass().getResourceAsStream("/fonts/LoungeBait-JpVa.ttf"));
            Roboto = Font.createFont(Font.TRUETYPE_FONT,
                    obj.getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"));
            RobotoBold = Font.createFont(Font.TRUETYPE_FONT,
                    obj.getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cargar la fuente
     * @param src InputStream de la fuente
     */
    public static void loadCasino(InputStream src) {
        try {
            Casino = Font.createFont(Font.TRUETYPE_FONT, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
