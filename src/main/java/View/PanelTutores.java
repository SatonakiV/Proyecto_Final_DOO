package View;

import Model.entidades.Tutor;
import Model.entidades.Materia;
import Model.entidades.BloqueHorario;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel con la tabla de tutores y los botones para agregar, editar y eliminar. Se
 * refresca solo cuando el Modelo notifica un cambio relevante sobre tutores.
 */
public class PanelTutores extends JPanel implements modelObserver {

    private controller.TutorController controller;
    private JTable tablaTutores;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    /**
     * @param controller controlador al que se delegan las operaciones sobre tutores
     */
    public PanelTutores(controller.TutorController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        inicializarBotones();
        inicializarTabla();
        cargarDatos();
    }

    /**
     * Crea los botones de agregar, editar y eliminar tutor, y conecta sus acciones.
     */
    private void inicializarBotones() {
        JPanel panelNorte = new JPanel();

        btnAgregar = new JButton("Agregar Tutor");
        btnEditar = new JButton("Editar Seleccionado");
        btnEliminar = new JButton("Eliminar Seleccionado");

        btnAgregar.addActionListener(e -> {
            DialogoTutor dialogo = new DialogoTutor(null, this.controller);
            dialogo.setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int fila = tablaTutores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un tutor de la tabla primero");
                return;
            }
            String id = (String) modeloTabla.getValueAt(fila, 0);
            Tutor tutor = controller.buscarPorId(id);
            if (tutor != null) {
                DialogoTutor dialogo = new DialogoTutor(null, this.controller, tutor);
                dialogo.setVisible(true);
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaTutores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un tutor de la tabla primero");
                return;
            }
            String id = (String) modeloTabla.getValueAt(fila, 0);
            int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este tutor?");
            if (conf == JOptionPane.YES_OPTION) {
                controller.eliminarTutor(id);
            }
        });

        panelNorte.add(btnAgregar);
        panelNorte.add(btnEditar);
        panelNorte.add(btnEliminar);
        add(panelNorte, BorderLayout.NORTH);
    }

    /**
     * Crea la tabla de tutores, sin celdas editables directamente.
     */
    private void inicializarTabla() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Email", "Teléfono", "Materias", "Bloques"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTutores = new JTable(modeloTabla);
        add(new JScrollPane(tablaTutores), BorderLayout.CENTER);
    }

    /**
     * Vacía y vuelve a llenar la tabla con los tutores activos actuales.
     */
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Tutor> lista = controller.obtenerTodos();
        for (Tutor t : lista) {
            int numMaterias = t.getMaterias().size();
            int numBloques = t.getBloquesDisponibilidad().size();
            Object[] fila = {
                    t.getId(),
                    t.getNombre(),
                    t.getApellido(),
                    t.getEmail(),
                    t.getTelefono(),
                    numMaterias + " materia(s)",
                    numBloques + " bloque(s)"
            };
            modeloTabla.addRow(fila);
        }
    }

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {
        if (evento == eventoModelo.TUTOR_AGREGADO ||
                evento == eventoModelo.TUTOR_MODIFICADO ||
                evento == eventoModelo.TUTOR_ELIMINADO) {
            cargarDatos();
        }
    }
}
