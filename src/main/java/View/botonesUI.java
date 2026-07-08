package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Utilidad para aplicar un estilo visual consistente a los botones de la aplicación:
 * fondo negro, texto blanco y un resaltado al pasar el mouse.
 */
public final class botonesUI {

    private static final Color NEGRO_BASE = new Color(24, 24, 26);
    private static final Color NEGRO_HOVER = new Color(46, 46, 50);
    private static final Color BLANCO_TEXTO = Color.WHITE;

    private botonesUI() {}

    /**
     * @param boton botón al que se le aplica el estilo negro
     */
    public static void pintarBoton(JButton boton) {
        boton.setBackground(NEGRO_BASE);
        boton.setForeground(BLANCO_TEXTO);
        boton.setFont(boton.getFont().deriveFont(Font.BOLD));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(NEGRO_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(NEGRO_BASE);
            }
        });
    }
}
