package View;

import Model.entidades.Estudiante;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import View.DialogoEstudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEstudiantes extends JPanel implements modelObserver {

    private controller.EstudianteController controller;
    private javax.swing.JTable tablaEstudiantes;
    private javax.swing.table.DefaultTableModel modeloTabla;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEditar;

    public PanelEstudiantes(controller.EstudianteController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        inicializarBotones();

        inicializarTabla();

        cargarDatos();

    }

    private void inicializarBotones() {

        JPanel panelNorte = new javax.swing.JPanel();

        btnAgregar = new JButton("Agregar Estudiante");
        btnEditar = new JButton("Editar Seleccionado");
        btnEliminar = new JButton("Eliminar Seleccionado");

        btnAgregar.addActionListener(e -> {

            DialogoEstudiante dialogo = new DialogoEstudiante(null, controller);
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

            DialogoEstudiante dialogo = new DialogoEstudiante(null, controller, est);
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

    private void cargarDatos(){

        modeloTabla.setRowCount(0);

        List<Estudiante> lista = controller.obtenerTodos();

        for(Estudiante e : lista){
            Object[] fila = {e.getId(), e.getNombre(), e.getApellido(), e.getEmail(), e.getTelefono()};
            modeloTabla.addRow(fila);
        }



    }


    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {

        if ((evento == eventoModelo.ESTUDIANTE_AGREGADO) ||
                (evento == eventoModelo.ESTUDIANTE_MODIFICADO) ||
                (evento == eventoModelo.ESTUDIANTE_ELIMINADO)){
            cargarDatos();
        }


    }
}
