package View;

import Model.entidades.*;
import Model.enums.diaSemana;
import controller.EstudianteController;
import controller.ReservaController;
import controller.TutorController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

/**
 * Diálogo con el flujo de dos pasos para agendar una clase: primero se arma un
 * criterio de búsqueda y se buscan tutores disponibles, y recién cuando hay resultados
 * se habilita agendar la clase con el tutor elegido. Separar la búsqueda del
 * agendamiento evita reservar a ciegas sin saber antes si el tutor tiene cupo.
 */
public class DialogoReserva extends JDialog {

    private ReservaController reservaController;
    private EstudianteController estudianteController;
    private TutorController tutorController;

    private JComboBox<Estudiante> cbEstudiantes;
    private JComboBox<String> cbMateria;
    private JComboBox<diaSemana> cbDias;
    private JComboBox<String> cbHoraInicio;
    private JComboBox<String> cbHoraFin;
    private JTextArea txtNotas;

    private JButton btnBuscarTutores;
    private JComboBox<Tutor> cbTutoresDisponibles;
    private JButton btnAgendar;

    /**
     * @param parent ventana padre del diálogo
     * @param resCtrl controlador al que se delegan la búsqueda de tutores y el agendamiento de la reserva
     * @param estCtrl controlador usado para llenar el combo de estudiantes
     * @param tutCtrl controlador usado para llenar el combo de materias
     */
    public DialogoReserva(JFrame parent,
                          ReservaController resCtrl,
                          EstudianteController estCtrl,
                          TutorController tutCtrl) {
        super(parent, "Buscador y Agendamiento de Reservas", true);
        this.reservaController = resCtrl;
        this.estudianteController = estCtrl;
        this.tutorController = tutCtrl;

        setSize(450, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarFormulario();
        inicializarPanelResultados();
        cargarDatosBase();
        configurarAccionesBotones();
    }

    /**
     * Crea el formulario con los criterios de búsqueda: estudiante, materia, día, horario y notas.
     */
    private void inicializarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Criterios de Búsqueda"));

        cbEstudiantes = new JComboBox<>();
        cbMateria = new JComboBox<>();
        cbDias = new JComboBox<>(diaSemana.values());
        
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        cbHoraInicio = new JComboBox<>(horas);
        cbHoraFin = new JComboBox<>(horas);
        txtNotas = new JTextArea(2, 20);

        panelForm.add(new JLabel("Seleccionar Estudiante:")); panelForm.add(cbEstudiantes);
        panelForm.add(new JLabel("Materia a recibir:")); panelForm.add(cbMateria);
        panelForm.add(new JLabel("Día de la semana:")); panelForm.add(cbDias);
        panelForm.add(new JLabel("Hora Inicio:")); panelForm.add(cbHoraInicio);
        panelForm.add(new JLabel("Hora Fin:")); panelForm.add(cbHoraFin);
        panelForm.add(new JLabel("Notas/Observaciones:")); panelForm.add(new JScrollPane(txtNotas));

        add(panelForm, BorderLayout.NORTH);
    }

    /**
     * Crea el panel con el botón de buscar tutores, el combo de resultados y el botón de agendar,
     * ambos deshabilitados hasta que haya una búsqueda con resultados.
     */
    private void inicializarPanelResultados() {
        JPanel panelResultados = new JPanel(new GridLayout(3, 1, 5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados y Agendamiento"));

        btnBuscarTutores = new JButton("1. Buscar Tutores Disponibles");
        cbTutoresDisponibles = new JComboBox<>();
        cbTutoresDisponibles.setEnabled(false);

        btnAgendar = new JButton("2. Agendar Clase con este Tutor");
        btnAgendar.setEnabled(false);
        botonesUI.pintarBoton(btnBuscarTutores);
        botonesUI.pintarBoton(btnAgendar);

        panelResultados.add(btnBuscarTutores);
        panelResultados.add(cbTutoresDisponibles);
        panelResultados.add(btnAgendar);

        add(panelResultados, BorderLayout.CENTER);
    }

    /**
     * Llena los combos de estudiante y materia con los datos actuales del Modelo.
     */
    private void cargarDatosBase() {
        List<Estudiante> estudiantes = estudianteController.obtenerTodos();
        for (Estudiante e : estudiantes) {
            cbEstudiantes.addItem(e);
        }

        List<String> materias = tutorController.obtenerMateriasUnicas();
        for (String m : materias) {
            cbMateria.addItem(m);
        }
    }

    /**
     * Conecta la acción de buscar tutores disponibles y la acción de agendar la clase
     * con el tutor seleccionado.
     */
    private void configurarAccionesBotones() {
        btnBuscarTutores.addActionListener(e -> {
            String materia = (String) cbMateria.getSelectedItem();
            diaSemana dia = (diaSemana) cbDias.getSelectedItem();
            
            LocalTime inicio = LocalTime.parse((String) cbHoraInicio.getSelectedItem());
            LocalTime fin = LocalTime.parse((String) cbHoraFin.getSelectedItem());

            if (materia == null || materia.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una materia");
                return;
            }

            if (inicio.isAfter(fin) || inicio.equals(fin)) {
                JOptionPane.showMessageDialog(this, "La hora de inicio debe ser antes que la hora de fin");
                return;
            }

            List<Tutor> tutoresDisponibles = reservaController.buscarTutoresDisponibles(materia, dia, inicio, fin);
            
            cbTutoresDisponibles.removeAllItems();
            
            if (tutoresDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay tutores disponibles para esos criterios.");
                cbTutoresDisponibles.setEnabled(false);
                btnAgendar.setEnabled(false);
            } else {
                for (Tutor t : tutoresDisponibles) {
                    cbTutoresDisponibles.addItem(t);
                }
                cbTutoresDisponibles.setEnabled(true);
                btnAgendar.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Se encontraron " + tutoresDisponibles.size() + " tutores disponibles.");
            }
        });

        btnAgendar.addActionListener(e -> {
            Estudiante estudiante = (Estudiante) cbEstudiantes.getSelectedItem();
            Tutor tutor = (Tutor) cbTutoresDisponibles.getSelectedItem();
            String nombreMateria = (String) cbMateria.getSelectedItem();
            diaSemana dia = (diaSemana) cbDias.getSelectedItem();
            LocalTime inicio = LocalTime.parse((String) cbHoraInicio.getSelectedItem());
            LocalTime fin = LocalTime.parse((String) cbHoraFin.getSelectedItem());
            String notas = txtNotas.getText();

            if (estudiante == null || tutor == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y un tutor");
                return;
            }

            Materia materiaSeleccionada = null;
            for (Materia m : tutor.getMaterias()) {
                if (m.getNombre().equalsIgnoreCase(nombreMateria)) {
                    materiaSeleccionada = m;
                    break;
                }
            }

            if (materiaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Error: El tutor no tiene registrada la materia seleccionada.");
                return;
            }

            BloqueHorario bloque = new BloqueHorario(dia, inicio, fin);

            String resultado = reservaController.agendarClase(estudiante, tutor, materiaSeleccionada, bloque, notas);
            
            JOptionPane.showMessageDialog(this, resultado);
            
            if (resultado.equals("Reserva guardada exitosamente.")) {
                dispose(); 
            }
        });
    }
}
