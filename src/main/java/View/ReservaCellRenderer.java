package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renderer para la tabla de reservas: colorea la fila completa según el estado de
 * la reserva (verde=activa, rojo=cancelada, gris=completada). Lee el estado desde
 * la columna llamada "Estado" comparando por texto, por lo que funciona tanto con
 * "ACTIVA" como con "Activa". Si esa columna no existe, no altera los colores.
 */
public class ReservaCellRenderer extends DefaultTableCellRenderer {

    private static final Color VERDE = new Color(200, 230, 201); // ACTIVA
    private static final Color ROJO  = new Color(255, 205, 210); // CANCELADA
    private static final Color GRIS  = new Color(224, 224, 224); // COMPLETADA
    private static final Color TEXTO = new Color(33, 33, 33);

    /**
     * Pinta cada celda de la tabla tomando como base el renderer por defecto y
     * cambiando el color de fondo de la fila según el estado de la reserva. Cuando
     * la fila está seleccionada, respeta el resaltado de selección de la tabla.
     *
     * @param table tabla que está pintando la celda
     * @param value valor de la celda actual
     * @param isSelected true si la celda está seleccionada
     * @param hasFocus true si la celda tiene el foco
     * @param row índice de la fila que se está pintando
     * @param column índice de la columna que se está pintando
     * @return el componente configurado con el color de fondo según el estado de la reserva
     */
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

    /**
     * Busca la columna llamada "Estado" dentro de la tabla y devuelve el valor de
     * esa fila en mayúsculas, para poder compararlo sin importar cómo esté escrito.
     *
     * @param table tabla en la que se busca la columna de estado
     * @param row índice de la fila cuyo estado se quiere obtener
     * @return el estado de la fila en mayúsculas, o cadena vacía si no hay columna "Estado"
     */
    private String obtenerEstadoDeFila(JTable table, int row) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if ("Estado".equalsIgnoreCase(table.getColumnName(i))) {
                Object val = table.getValueAt(row, i);
                return val == null ? "" : val.toString().trim().toUpperCase();
            }
        }
        return "";
    }

    /**
     * Traduce el texto de un estado al color de fondo que le corresponde.
     *
     * @param estado texto del estado de la reserva (en mayúsculas)
     * @return el color asociado al estado, o null si el estado es vacío o desconocido
     */
    private Color colorPorEstado(String estado) {
        if (estado == null || estado.isEmpty()) return null;
        if (estado.startsWith("ACTIV"))   return VERDE;
        if (estado.startsWith("CANCEL"))  return ROJO;
        if (estado.startsWith("COMPLET")) return GRIS;
        return null;
    }
}