/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */

package pokerView;

import classicPoker.*;
import classicPoker.Carta.Palos;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

/**
 * Clase estatica encargada de cargar las imagenes de las cartas y devolver la imagen de una carta en especifico
 */
public class CardImage {
    private static BufferedImage cardsImage;
    public static ImageIcon cartaTapada, cartaTapadaMax;
    private static final int top = 23;
    private static final int left = 10;

    private static final int cardWidth = 103;
    private static final int cardHeight = 157;

    private static final int verticalSpace = 8;
    private static final int horizontalSpace = 11;

    private static Object obj;

    /**
     * Cargar las imagenes
     * @param obj referencia de clase para acceder a los recursos
     */
    public static void loadImage(Object lobj) {
        try {
            obj = lobj;
            cartaTapada = new ImageIcon(obj.getClass().getResource("/images/carta-tapada.png"));
            cartaTapadaMax = new ImageIcon(obj.getClass().getResource("/images/carta-tapada.max.png"));
            cardsImage = ImageIO.read(obj.getClass().getResource("/images/cards.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna la imagen de una carta en el tamaño especificado
     * @param carta
     * @param reSize
     * @return imagen
     */
    public static ImageIcon get(Carta carta, Dimension reSize) {
        String prefijo = "";
        int x;

        if (carta.palo == Palos.treboles)
            prefijo = "trebol-";
        if (carta.palo == Palos.corazones)
            prefijo = "corazon-";
        if (carta.palo == Palos.picas)
            prefijo = "pica-";
        if (carta.palo == Palos.diamantes)
            prefijo = "diamante-";

        x = carta.numero;
        if (carta.numero == 14)
            x = 1;
        String ss = "/images/cards" + String.valueOf((int) reSize.getWidth()) + "/" + prefijo + x + ".png";
        return new ImageIcon(obj.getClass().getResource(ss));
    }
    /*public static ImageIcon get(Carta carta, Dimension reSize) {
        int x = cardWidth + horizontalSpace, y = cardHeight + verticalSpace;
    
        if (carta.palo == Palos.treboles)
            y = 0 * y;
        if (carta.palo == Palos.corazones)
            y = 1 * y;
        if (carta.palo == Palos.picas)
            y = 2 * y;
        if (carta.palo == Palos.diamantes)
            y = 3 * y;
    
        x = x * (carta.numero - 1);
        if (carta.numero == 14)
            x = 0;
    
        return new ImageIcon(resize(cardsImage.getSubimage(left + x, top + y, cardWidth, cardHeight),
                (int) reSize.getWidth(), (int) reSize.getHeight()));
    }*/

    /**
     * redimensiona el tamaño de una imagen almacenada en un BufferedImage
     * @param img
     * @param newW
     * @param newH
     * @return BufferedImage
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

}
