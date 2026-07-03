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

public class DialogoReserva extends JDialog {

    private ReservaController reservaController;
    private EstudianteController estudianteController;
    private TutorController tutorController;

    private JComboBox<Estudiante> cbEstudiantes;
    private JTextField txtMateria;
    private JComboBox<diaSemana> cbDias;
    private JComboBox<String> cbHoraInicio;
    private JComboBox<String> cbHoraFin;
    private JTextArea txtNotas;

    private JButton btnBuscarTutores;
    private JComboBox<Tutor> cbTutoresDisponibles;
    private JButton btnAgendar;

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

    private void inicializarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Criterios de Búsqueda"));

        cbEstudiantes = new JComboBox<>();
        txtMateria = new JTextField();
        cbDias = new JComboBox<>(diaSemana.values());
        
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        cbHoraInicio = new JComboBox<>(horas);
        cbHoraFin = new JComboBox<>(horas);
        txtNotas = new JTextArea(2, 20);

        panelForm.add(new JLabel("Seleccionar Estudiante:")); panelForm.add(cbEstudiantes);
        panelForm.add(new JLabel("Materia a recibir:")); panelForm.add(txtMateria);
        panelForm.add(new JLabel("Día de la semana:")); panelForm.add(cbDias);
        panelForm.add(new JLabel("Hora Inicio:")); panelForm.add(cbHoraInicio);
        panelForm.add(new JLabel("Hora Fin:")); panelForm.add(cbHoraFin);
        panelForm.add(new JLabel("Notas/Observaciones:")); panelForm.add(new JScrollPane(txtNotas));

        add(panelForm, BorderLayout.NORTH);
    }

    private void inicializarPanelResultados() {
        JPanel panelResultados = new JPanel(new GridLayout(3, 1, 5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados y Agendamiento"));

        btnBuscarTutores = new JButton("1. Buscar Tutores Disponibles");
        cbTutoresDisponibles = new JComboBox<>();
        cbTutoresDisponibles.setEnabled(false);
        
        btnAgendar = new JButton("2. Agendar Clase con este Tutor");
        btnAgendar.setEnabled(false);

        panelResultados.add(btnBuscarTutores);
        panelResultados.add(cbTutoresDisponibles);
        panelResultados.add(btnAgendar);

        add(panelResultados, BorderLayout.CENTER);
    }

    private void cargarDatosBase() {
        List<Estudiante> estudiantes = estudianteController.obtenerTodos();
        for (Estudiante e : estudiantes) {
            cbEstudiantes.addItem(e);
        }
    }

    private void configurarAccionesBotones() {
        btnBuscarTutores.addActionListener(e -> {
            String materia = txtMateria.getText().trim();
            diaSemana dia = (diaSemana) cbDias.getSelectedItem();
            
            LocalTime inicio = LocalTime.parse((String) cbHoraInicio.getSelectedItem());
            LocalTime fin = LocalTime.parse((String) cbHoraFin.getSelectedItem());

            if (materia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe escribir una materia");
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
            String nombreMateria = txtMateria.getText().trim();
            diaSemana dia = (diaSemana) cbDias.getSelectedItem();
            LocalTime inicio = LocalTime.parse((String) cbHoraInicio.getSelectedItem());
            LocalTime fin = LocalTime.parse((String) cbHoraFin.getSelectedItem());
            String notas = txtNotas.getText();

            if (estudiante == null || tutor == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y un tutor");
                return;
            }

            Materia materia = new Materia(nombreMateria, 0, 0); 
            BloqueHorario bloque = new BloqueHorario(dia, inicio, fin);

            String resultado = reservaController.agendarClase(estudiante, tutor, materia, bloque, notas);
            
            JOptionPane.showMessageDialog(this, resultado);
            
            if (resultado.equals("Reserva guardada exitosamente.")) {
                dispose(); 
            }
        });
    }
}
