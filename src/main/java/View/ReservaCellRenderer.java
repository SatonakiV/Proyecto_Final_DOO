package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renderer para la tabla de reservas: colorea la fila completa según el estado.
 *   ACTIVA     -> verde
 *   CANCELADA  -> rojo
 *   COMPLETADA -> gris
 *
 * Lee el estado desde la columna llamada "Estado" (compara por texto, así funciona
 * tanto con "ACTIVA" como con "Activa"). Si esa columna no existe, no altera colores.
 */
public class ReservaCellRenderer extends DefaultTableCellRenderer {

    private static final Color VERDE = new Color(200, 230, 201); // ACTIVA
    private static final Color ROJO  = new Color(255, 205, 210); // CANCELADA
    private static final Color GRIS  = new Color(224, 224, 224); // COMPLETADA
    private static final Color TEXTO = new Color(33, 33, 33);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        Color fondo = colorPorEstado(obtenerEstadoDeFila(table, row));

        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(fondo != null ? fondo : table.getBackground());
            c.setForeground(TEXTO);
        }
        return c;
    }

    /** Busca la columna "Estado" y devuelve su valor en mayúsculas; "" si no existe. */
    private String obtenerEstadoDeFila(JTable table, int row) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if ("Estado".equalsIgnoreCase(table.getColumnName(i))) {
                Object val = table.getValueAt(row, i);
                return val == null ? "" : val.toString().trim().toUpperCase();
            }
        }
        return "";
    }

    private Color colorPorEstado(String estado) {
        if (estado == null || estado.isEmpty()) return null;
        if (estado.startsWith("ACTIV"))   return VERDE;
        if (estado.startsWith("CANCEL"))  return ROJO;
        if (estado.startsWith("COMPLET")) return GRIS;
        return null;
    }
}