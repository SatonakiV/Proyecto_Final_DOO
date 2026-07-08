package View;

import Model.entidades.Tutor;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer para mostrar tutores en un JComboBox o JList con el formato
 * "Nombre Apellido — N materias", en lugar del texto por defecto del toString().
 */
public class TutorCellRenderer extends JLabel implements ListCellRenderer<Tutor> {

    /**
     * Crea el renderer dejándolo opaco para que se pinte el color de fondo y
     * con un margen interno para separar el texto de los bordes de la celda.
     */
    public TutorCellRenderer() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    /**
     * Arma el componente que se dibuja para cada tutor de la lista/combo, con su
     * nombre completo y la cantidad de materias que dicta, y aplica los colores
     * de selección según corresponda.
     *
     * @param list lista o combo que está pintando la celda
     * @param tutor tutor a mostrar en esta celda (puede ser null si no hay selección)
     * @param index posición del elemento dentro de la lista
     * @param isSelected true si la celda está seleccionada
     * @param cellHasFocus true si la celda tiene el foco
     * @return este mismo JLabel configurado con el texto y los colores de la celda
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Tutor> list,
                                                  Tutor tutor,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        if (tutor == null) {
            setText("(Sin selección)");
        } else {
            int nMaterias = tutor.getMaterias() == null ? 0 : tutor.getMaterias().size();
            setText(tutor.getNombreCompleto() + " — " + nMaterias
                    + (nMaterias == 1 ? " materia" : " materias"));
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setFont(list.getFont());
        setEnabled(list.isEnabled());
        return this;
    }
}