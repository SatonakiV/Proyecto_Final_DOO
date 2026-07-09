package View;

import Model.entidades.Estudiante;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import View.DialogoEstudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel con la tabla de estudiantes y los botones para agregar, editar y eliminar. Se
 * refresca solo cuando el Modelo notifica un cambio relevante sobre estudiantes.
 */
public class PanelEstudiantes extends JPanel implements modelObserver {

    private controller.EstudianteController controller;
    private javax.swing.JTable tablaEstudiantes;
    private javax.swing.table.DefaultTableModel modeloTabla;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEditar;

    /**
     * @param controller controlador al que se delegan las operaciones sobre estudiantes
     */
    public PanelEstudiantes(controller.EstudianteController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        inicializarBotones();

        inicializarTabla();

        cargarDatos();

    }

    /**
     * @return la ventana principal que contiene a este panel, o null si aún no está en pantalla
     */
    private JFrame obtenerVentanaPadre() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana instanceof JFrame) {
            return (JFrame) ventana;
        }
        return null;
    }

    /**
     * Crea los botones de agregar, editar y eliminar estudiante, y conecta sus acciones.
     */
    private void inicializarBotones() {

        JPanel panelNorte = new javax.swing.JPanel();

        btnAgregar = new JButton("Agregar Estudiante");
        btnEditar = new JButton("Editar Seleccionado");
        btnEliminar = new JButton("Eliminar Seleccionado");
        botonesUI.pintarBoton(btnAgregar);
        botonesUI.pintarBoton(btnEditar);
        botonesUI.pintarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> {

            DialogoEstudiante dialogo = new DialogoEstudiante(obtenerVentanaPadre(), controller);
            dialogo.setVisible(true);

        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tablaEstudiantes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla primero");
                return;
            }

            String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

            Estudiante est = controller.buscarPorId(id);

            DialogoEstudiante dialogo = new DialogoEstudiante(obtenerVentanaPadre(), controller, est);
            dialogo.setVisible(true);


        });


        btnEliminar.addActionListener(e -> {

            int filaSeleccionada = tablaEstudiantes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla primero");
                return;
            }

            String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar a este estudiante?");
            if (confirmacion == JOptionPane.YES_OPTION) {
                String resultado = controller.eliminar(id);
                JOptionPane.showMessageDialog(this, resultado);
            }
        });


        panelNorte.add(btnAgregar);
        panelNorte.add(btnEditar);
        panelNorte.add(btnEliminar);
        add(panelNorte, java.awt.BorderLayout.NORTH);

    }



    /**
     * Crea la tabla de estudiantes, sin celdas editables directamente.
     */
    private void inicializarTabla() {

        String[] columnas = {"ID", "Nombre", "Apellido", "Email", "Teléfono"};

        modeloTabla = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int filas, int columnas){
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);

        JScrollPane scrollPane = new  JScrollPane(tablaEstudiantes);

        add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Vacía y vuelve a llenar la tabla con los estudiantes activos actuales.
     */
    private void cargarDatos(){

        modeloTabla.setRowCount(0);

        List<Estudiante> lista = controller.obtenerTodos();

        for(Estudiante e : lista){
            Object[] fila = {e.getId(), e.getNombre(), e.getApellido(), e.getEmail(), e.getTelefono()};
            modeloTabla.addRow(fila);
        }



    }


    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {

        if ((evento == eventoModelo.ESTUDIANTE_AGREGADO) ||
                (evento == eventoModelo.ESTUDIANTE_MODIFICADO) ||
                (evento == eventoModelo.ESTUDIANTE_ELIMINADO)){
            cargarDatos();
        }


    }
}