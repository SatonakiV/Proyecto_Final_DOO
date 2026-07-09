package View;

import Model.entidades.BloqueHorario;
import Model.entidades.Reserva;
import Model.enums.diaSemana;
import controller.ReservaController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

/**
 * Diálogo para cambiar el día y horario de una reserva ya existente. Muestra los datos
 * fijos de la reserva (estudiante, tutor, materia) solo como referencia, y deja editar
 * únicamente el bloque de horario, que es lo único que ReservaController.modificarReserva
 * permite cambiar.
 */
public class DialogoModificarReserva extends JDialog {

    private static final String[] HORAS = {"08:00", "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};

    private final ReservaController controller;
    private final Reserva reserva;

    private JComboBox<diaSemana> cbDia;
    private JComboBox<String> cbHoraInicio;
    private JComboBox<String> cbHoraFin;
    private JButton btnGuardar;

    /**
     * @param parent ventana padre del diálogo
     * @param controller controlador al que se delega el guardado del nuevo horario
     * @param reserva reserva a modificar, usada para precargar sus datos actuales
     */
    public DialogoModificarReserva(JFrame parent, ReservaController controller, Reserva reserva) {
        super(parent, "Modificar Horario de Reserva", true);
        this.controller = controller;
        this.reserva = reserva;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarFormulario();
        configurarAccionBoton();
    }

    /**
     * Crea el formulario con los datos fijos de la reserva y los combos editables de
     * día y horario, precargados con el bloque de horario actual de la reserva.
     */
    private void inicializarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nuevo Horario"));

        cbDia = new JComboBox<>(diaSemana.values());
        cbHoraInicio = new JComboBox<>(HORAS);
        cbHoraFin = new JComboBox<>(HORAS);

        BloqueHorario actual = reserva.getBloqueHorario();
        cbDia.setSelectedItem(actual.getDia());
        cbHoraInicio.setSelectedItem(actual.getHoraInicio().toString());
        cbHoraFin.setSelectedItem(actual.getHoraFin().toString());

        panelForm.add(new JLabel("Estudiante:"));
        panelForm.add(new JLabel(reserva.getEstudiante().getNombreCompleto()));
        panelForm.add(new JLabel("Tutor:"));
        panelForm.add(new JLabel(reserva.getTutor().getNombreCompleto()));
        panelForm.add(new JLabel("Materia:"));
        panelForm.add(new JLabel(reserva.getMateria().getNombre()));
        panelForm.add(new JLabel("Día:"));
        panelForm.add(cbDia);
        panelForm.add(new JLabel("Hora Inicio:"));
        panelForm.add(cbHoraInicio);
        panelForm.add(new JLabel("Hora Fin:"));
        panelForm.add(cbHoraFin);

        add(panelForm, BorderLayout.CENTER);

        btnGuardar = new JButton("Guardar Cambios");
        botonesUI.pintarBoton(btnGuardar);
        JPanel panelSur = new JPanel();
        panelSur.add(btnGuardar);
        add(panelSur, BorderLayout.SOUTH);
    }

    /**
     * Conecta la acción de guardar: valida el rango horario, arma el nuevo bloque y
     * se lo pasa al controlador, mostrando el resultado y cerrando el diálogo si
     * la modificación tuvo éxito.
     */
    private void configurarAccionBoton() {
        btnGuardar.addActionListener(e -> {
            diaSemana dia = (diaSemana) cbDia.getSelectedItem();
            LocalTime inicio = LocalTime.parse((String) cbHoraInicio.getSelectedItem());
            LocalTime fin = LocalTime.parse((String) cbHoraFin.getSelectedItem());

            if (inicio.isAfter(fin) || inicio.equals(fin)) {
                JOptionPane.showMessageDialog(this, "La hora de inicio debe ser antes que la hora de fin");
                return;
            }

            BloqueHorario nuevoBloque = new BloqueHorario(dia, inicio, fin);
            String resultado = controller.modificarReserva(reserva.getId(), nuevoBloque);

            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.equals("Reserva modificada con éxito.")) {
                dispose();
            }
        });
    }
}
