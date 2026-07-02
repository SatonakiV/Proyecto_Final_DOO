package View;

import Model.entidades.Estudiante;
import controller.EstudianteController;

import javax.swing.*;
import java.awt.*;

public class DialogoEstudiante extends JDialog {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private boolean editar = false;
    private String idEditar = null;


    private EstudianteController controller;

    public DialogoEstudiante(JFrame frame, EstudianteController controller) {
        super(frame, "Nuevo Estudiante", true);
        this.controller = controller;
        inicializarComponentes();
    }


    public DialogoEstudiante(JFrame frame, EstudianteController controller, Estudiante estudianteAEditar) {
        super(frame, "Editar Estudiante", true);
        this.controller = controller;
        this.editar = true;
        this.idEditar = estudianteAEditar.getId();

        inicializarComponentes();

        txtNombre.setText(estudianteAEditar.getNombre());
        txtApellido.setText(estudianteAEditar.getApellido());
        txtEmail.setText(estudianteAEditar.getEmail());
        txtTelefono.setText(estudianteAEditar.getTelefono());

    }



    private void inicializarComponentes() {

        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5,2,10,10));

        txtNombre = new JTextField(); txtApellido = new JTextField();
        txtEmail = new JTextField(); txtTelefono = new JTextField();

        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Apellido:")); add(txtApellido);
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Telefono:")); add(txtTelefono);

        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        add(btnAceptar); add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        btnAceptar.addActionListener(e -> {

            String nombre = txtNombre.getText(), apellido = txtApellido.getText();
            String email = txtEmail.getText(), telefono = txtTelefono.getText();

            if(nombre.isEmpty() || apellido.isEmpty()){
                JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios");
                return;
            }

            String resultado = "";
            if(editar){
                resultado = controller.modificar(idEditar, nombre, apellido, email, telefono);

            } else {
                controller.registrar(nombre, apellido, email, telefono);
            }

            JOptionPane.showMessageDialog(this, resultado);
            if(resultado.equals("Éxito")) dispose();
        });




    }

}
