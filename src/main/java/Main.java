import View.MainFrame;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {
    /**
     * Crea y muestra la ventana principal en el hilo de eventos de Swing.
     *
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame ventana = new MainFrame();
                ventana.setVisible(true);
            }
        });
    }

}
