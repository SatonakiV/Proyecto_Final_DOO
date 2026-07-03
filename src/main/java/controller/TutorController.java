package controller;

import Model.entidades.BloqueHorario;
import Model.entidades.Materia;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import Model.services.TutorService;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.time.LocalTime;
import java.util.List;

public class TutorController {
    private TutorService tutorService;
    private JFrame mainFrame; // Usamos JFrame genérico temporalmente para evitar errores antes de crear el MainFrame real

    public TutorController(TutorService tutorService, JFrame mainFrame) {
        this.tutorService = tutorService;
        this.mainFrame = mainFrame;
    }

    public void agregarTutor(String nombre, String apellido, String email, String telefono) {
        try {
            tutorService.agregarTutor(nombre, apellido, email, telefono);
            // Si funciona, el observer actualizará la tabla automáticamente
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar tutor", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificarTutor(String id, String nombre, String apellido, String email, String telefono) {
        try {
            tutorService.modificarTutor(id, nombre, apellido, email, telefono);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al modificar tutor", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarTutor(String id) {
        // Confirmación de borrado
        int confirmacion = JOptionPane.showConfirmDialog(mainFrame,
                "¿Está seguro que desea eliminar este tutor?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                tutorService.eliminarTutor(id);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al eliminar tutor", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void agregarMateriaATutor(String tutorId, String nombreMateria, double tarifa, int cupoMax) {
        try {
            Materia materia = new Materia(nombreMateria, tarifa, cupoMax);
            tutorService.agregarMateriaATutor(tutorId, materia);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar materia", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void agregarBloqueATutor(String tutorId, diaSemana dia, LocalTime inicio, LocalTime fin) {
        try {
            BloqueHorario bloque = new BloqueHorario(dia, inicio, fin);
            tutorService.agregarBloqueATutor(tutorId, bloque);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar bloque", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Tutor> obtenerTodos() {
        return tutorService.obtenerTodos();
    }

    public List<String> obtenerMateriasUnicas() {
        return tutorService.obtenerMateriasUnicas();
    }

    public Tutor buscarPorId(String id) {
        try {
            return tutorService.buscarPorId(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}