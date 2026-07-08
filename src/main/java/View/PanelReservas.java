package View;

import Model.entidades.Reserva;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import controller.EstudianteController;
import controller.ReservaController;
import controller.TutorController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel con la tabla de reservas y los botones para agendar una nueva reserva o anular
 * la seleccionada. Se refresca solo cuando el Modelo notifica un cambio relevante sobre
 * reservas.
 */
public class PanelReservas extends JPanel implements modelObserver {

    private ReservaController controller;
    private EstudianteController estudianteController;
    private TutorController tutorController;

    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevaReserva;
    private JButton btnAnularReserva;

    /**
     * @param controller controlador al que se delegan las operaciones sobre reservas
     * @param estCtrl controlador de estudiantes, usado por el diálogo de nueva reserva
     * @param tutCtrl controlador de tutores, usado por el diálogo de nueva reserva
     */
    public PanelReservas(ReservaController controller,
                         EstudianteController estCtrl,
                         TutorController tutCtrl) {
        this.controller = controller;
        this.estudianteController = estCtrl;
        this.tutorController = tutCtrl;

        setLayout(new BorderLayout());

        inicializarBotones();
        inicializarTabla();
        cargarDatos();
    }

    /**
     * Crea los botones de nueva reserva y anular reserva, y conecta sus acciones.
     */
    private void inicializarBotones() {
        JPanel PanelNorte = new JPanel();

        btnNuevaReserva = new JButton("Nueva Reserva (Buscar Tutor)");
        btnAnularReserva = new JButton("Anular Reserva Seleccionada");
        botonesUI.pintarBoton(btnNuevaReserva);
        botonesUI.pintarBoton(btnAnularReserva);

        btnAnularReserva.addActionListener(e -> {
            int fila = tablaReservas.getSelectedRow();
            if(fila == -1){
                JOptionPane.showMessageDialog(this, "Por favor seleccione una reserva primero");
                return;
            }

            String idReserva = (String) modeloTabla.getValueAt(fila, 0);

            int conf = JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas anular esta reserva?");
            if(conf == JOptionPane.YES_OPTION){
                String resultado = controller.anular(idReserva);
                JOptionPane.showMessageDialog(this, resultado);
            }
        });

        btnNuevaReserva.addActionListener(e -> {
            DialogoReserva dialogo = new DialogoReserva(null, this.controller, this.estudianteController, this.tutorController);
            dialogo.setVisible(true);
        });

        PanelNorte.add(btnNuevaReserva);
        PanelNorte.add(btnAnularReserva);
        add(PanelNorte, BorderLayout.NORTH);
    }

    /**
     * Crea la tabla de reservas, sin celdas editables directamente.
     */
    private void inicializarTabla() {
        String[] columnas = {"ID", "Estudiante", "Tutor", "Materia", "Día", "Horario", "Tarifa/hr", "Costo Total"};

        modeloTabla = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReservas = new JTable(modeloTabla);
        add(new JScrollPane(tablaReservas), BorderLayout.CENTER);
    }

    /**
     * Vacía y vuelve a llenar la tabla con las reservas activas actuales.
     */
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Reserva> lista = controller.obtenerTodas();

        for(Reserva r : lista){
            if (r.estaActiva()) {
                double tarifaPorHora = r.getMateria().getTarifaPorHora();
                double horas = r.getBloqueHorario().getDuracionMinutos() / 60.0;
                double costoTotal = tarifaPorHora * horas;

                Object[] fila = {
                    r.getId(),
                    r.getEstudiante().getNombreCompleto(),
                    r.getTutor().getNombreCompleto(),
                    r.getMateria().getNombre(),
                    r.getBloqueHorario().getDia().toString(),
                    r.getBloqueHorario().getHoraInicio() + " a " + r.getBloqueHorario().getHoraFin(),
                    String.format("$%.2f", tarifaPorHora),
                    String.format("$%.2f", costoTotal)
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {
        if(evento == eventoModelo.RESERVA_CREADA || evento == eventoModelo.RESERVA_CANCELADA){
            cargarDatos();
        }
    }
}
