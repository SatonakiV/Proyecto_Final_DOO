package View;

import Model.entidades.Tutor;
import Model.entidades.Materia;
import Model.entidades.BloqueHorario;
import Model.enums.diaSemana;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * Diálogo más completo de la aplicación: además de nombre, apellido, email y teléfono,
 * permite agregar materias y bloques de horario en dos tablas internas que se llenan
 * en memoria y recién se guardan contra el Modelo al presionar "Guardar". El mismo
 * diálogo sirve para crear o editar, según qué constructor se use.
 */
public class DialogoTutor extends JDialog {

    private controller.TutorController controller;
    private boolean modoEdicion = false;
    private String idAEditar = null;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;

    private JTable tablaMaterias;
    private DefaultTableModel modeloMaterias;
    private JTextField txtNombreMateria;
    private JTextField txtTarifa;
    private JTextField txtCupo;
    private JButton btnAgregarMateria;
    private JButton btnQuitarMateria;

    private JTable tablaHorarios;
    private DefaultTableModel modeloHorarios;
    private JComboBox<diaSemana> cbDia;
    private JComboBox<String> cbHoraInicio;
    private JComboBox<String> cbHoraFin;
    private JButton btnAgregarHorario;
    private JButton btnQuitarHorario;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private static final String[] HORAS = {
            "07:00","08:00","09:00","10:00","11:00","12:00",
            "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"
    };

    /**
     * Crea el diálogo en modo de registro de un tutor nuevo.
     *
     * @param parent ventana padre del diálogo
     * @param controller controlador al que se delegan las operaciones sobre el tutor
     */
    public DialogoTutor(JFrame parent, controller.TutorController controller) {
        super(parent, "Nuevo Tutor", true);
        this.controller = controller;
        inicializarComponentes();
    }

    /**
     * Crea el diálogo en modo de edición, precargando los campos, materias y bloques de horario del tutor dado.
     *
     * @param parent ventana padre del diálogo
     * @param controller controlador al que se delegan las operaciones sobre el tutor
     * @param tutor tutor cuyos datos se van a editar
     */
    public DialogoTutor(JFrame parent, controller.TutorController controller, Tutor tutor) {
        super(parent, "Editar Tutor", true);
        this.controller = controller;
        this.modoEdicion = true;
        this.idAEditar = tutor.getId();
        inicializarComponentes();
        txtNombre.setText(tutor.getNombre());
        txtApellido.setText(tutor.getApellido());
        txtEmail.setText(tutor.getEmail());
        txtTelefono.setText(tutor.getTelefono());
        for (Materia m : tutor.getMaterias()) {
            modeloMaterias.addRow(new Object[]{m.getNombre(), m.getTarifaPorHora(), m.getCupoMaximoEstudiantes()});
        }
        for (BloqueHorario b : tutor.getBloquesDisponibilidad()) {
            modeloHorarios.addRow(new Object[]{b.getDia().name(), b.getHoraInicio().toString(), b.getHoraFin().toString()});
        }
    }

    /**
     * Crea y ubica los tres paneles principales del diálogo: datos básicos, materias/horarios y botones.
     */
    private void inicializarComponentes() {
        setSize(600, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearPanelDatosBasicos(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    /**
     * @return el panel con los campos de nombre, apellido, email y teléfono del tutor
     */
    private JPanel crearPanelDatosBasicos() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Tutor"));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtEmail = new JTextField();
        txtTelefono = new JTextField();

        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Apellido:")); panel.add(txtApellido);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTelefono);
        return panel;
    }

    /**
     * @return el panel central que junta el panel de materias y el panel de horarios
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 0));
        panel.add(crearPanelMaterias());
        panel.add(crearPanelHorarios());
        return panel;
    }

    /**
     * @return el panel con la tabla de materias del tutor y los campos para agregar o quitar una materia
     */
    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Materias"));

        String[] cols = {"Materia", "Tarifa/hr", "Cupo"};
        modeloMaterias = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMaterias = new JTable(modeloMaterias);
        panel.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 3, 3));
        txtNombreMateria = new JTextField();
        txtTarifa = new JTextField();
        txtCupo = new JTextField();
        btnAgregarMateria = new JButton("Agregar");
        btnQuitarMateria = new JButton("Quitar");
        botonesUI.pintarBoton(btnAgregarMateria);
        botonesUI.pintarBoton(btnQuitarMateria);

        panelEntrada.add(new JLabel("Nombre:")); panelEntrada.add(txtNombreMateria);
        panelEntrada.add(new JLabel("Tarifa:")); panelEntrada.add(txtTarifa);
        panelEntrada.add(new JLabel("Cupo:")); panelEntrada.add(txtCupo);
        panelEntrada.add(btnAgregarMateria); panelEntrada.add(btnQuitarMateria);

        btnAgregarMateria.addActionListener(e -> {
            String nombre = txtNombreMateria.getText().trim();
            String tarifaStr = txtTarifa.getText().trim();
            String cupoStr = txtCupo.getText().trim();
            if (nombre.isEmpty() || tarifaStr.isEmpty() || cupoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos de la materia");
                return;
            }
            try {
                double tarifa = Double.parseDouble(tarifaStr);
                int cupo = Integer.parseInt(cupoStr);
                modeloMaterias.addRow(new Object[]{nombre, tarifa, cupo});
                txtNombreMateria.setText(""); txtTarifa.setText(""); txtCupo.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tarifa debe ser número y Cupo debe ser entero");
            }
        });

        btnQuitarMateria.addActionListener(e -> {
            int fila = tablaMaterias.getSelectedRow();
            if (fila != -1) modeloMaterias.removeRow(fila);
        });

        panel.add(panelEntrada, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * @return el panel con la tabla de bloques de horario del tutor y los campos para agregar o quitar un bloque
     */
    private JPanel crearPanelHorarios() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Horarios de Disponibilidad"));

        String[] cols = {"Día", "Inicio", "Fin"};
        modeloHorarios = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaHorarios = new JTable(modeloHorarios);
        panel.add(new JScrollPane(tablaHorarios), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 3, 3));
        cbDia = new JComboBox<>(diaSemana.values());
        cbHoraInicio = new JComboBox<>(HORAS);
        cbHoraFin = new JComboBox<>(HORAS);
        cbHoraFin.setSelectedIndex(1);
        btnAgregarHorario = new JButton("Agregar");
        btnQuitarHorario = new JButton("Quitar");
        botonesUI.pintarBoton(btnAgregarHorario);
        botonesUI.pintarBoton(btnQuitarHorario);

        panelEntrada.add(new JLabel("Día:")); panelEntrada.add(cbDia);
        panelEntrada.add(new JLabel("Inicio:")); panelEntrada.add(cbHoraInicio);
        panelEntrada.add(new JLabel("Fin:")); panelEntrada.add(cbHoraFin);
        panelEntrada.add(btnAgregarHorario); panelEntrada.add(btnQuitarHorario);

        btnAgregarHorario.addActionListener(e -> {
            String dia = ((diaSemana) cbDia.getSelectedItem()).name();
            String inicio = (String) cbHoraInicio.getSelectedItem();
            String fin = (String) cbHoraFin.getSelectedItem();
            if (inicio.compareTo(fin) >= 0) {
                JOptionPane.showMessageDialog(this, "La hora de inicio debe ser antes que la hora de fin");
                return;
            }
            modeloHorarios.addRow(new Object[]{dia, inicio, fin});
        });

        btnQuitarHorario.addActionListener(e -> {
            int fila = tablaHorarios.getSelectedRow();
            if (fila != -1) modeloHorarios.removeRow(fila);
        });

        panel.add(panelEntrada, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * @return el panel con los botones de guardar y cancelar, con la lógica para registrar o modificar el tutor y sus materias y horarios
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botonesUI.pintarBoton(btnGuardar);
        botonesUI.pintarBoton(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String email = txtEmail.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre, apellido y email son obligatorios");
                return;
            }

            if (modoEdicion) {
                controller.modificarTutor(idAEditar, nombre, apellido, email, telefono);
            } else {
                controller.agregarTutor(nombre, apellido, email, telefono);
                List<Tutor> tutores = controller.obtenerTodos();
                if (!tutores.isEmpty()) {
                    String nuevoId = tutores.get(tutores.size() - 1).getId();
                    for (int i = 0; i < modeloMaterias.getRowCount(); i++) {
                        String nomMat = (String) modeloMaterias.getValueAt(i, 0);
                        double tarifa = (double) modeloMaterias.getValueAt(i, 1);
                        int cupo = (int) modeloMaterias.getValueAt(i, 2);
                        controller.agregarMateriaATutor(nuevoId, nomMat, tarifa, cupo);
                    }
                    for (int i = 0; i < modeloHorarios.getRowCount(); i++) {
                        String diaStr = (String) modeloHorarios.getValueAt(i, 0);
                        String inicioStr = (String) modeloHorarios.getValueAt(i, 1);
                        String finStr = (String) modeloHorarios.getValueAt(i, 2);
                        controller.agregarBloqueATutor(
                                nuevoId,
                                diaSemana.valueOf(diaStr),
                                LocalTime.parse(inicioStr),
                                LocalTime.parse(finStr)
                        );
                    }
                }
            }
            dispose();
        });

        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }
}
