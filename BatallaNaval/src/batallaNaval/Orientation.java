/*
 * Programacion Interactiva
 * Mini proyecto 2: Juego de batalla naval.
 */
package batallaNaval;

/**
 * Enum Orientation: contiene las posibles orientaciones y un determinado angulo para rotar una imagen.
 */
public enum Orientation {
    DER(0), ABJ(90), IZQ(0), ARB(90);

    public final int angle;

    private Orientation(int angle) {
        this.angle = angle;
    }
}