package View;

import Model.entidades.Tutor;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer para mostrar tutores en JComboBox o JList con el formato:
 *   "Nombre Apellido — N materias"
 */
public class TutorCellRenderer extends JLabel implements ListCellRenderer<Tutor> {

    public TutorCellRenderer() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

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