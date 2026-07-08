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

/**
 * Traduce las acciones de la Vista sobre tutores en llamadas a TutorService, capturando
 * las excepciones de negocio y mostrando un JOptionPane de error en vez de dejarlas
 * propagarse.
 */
public class TutorController {
    private TutorService tutorService;
    private JFrame mainFrame; // Usamos JFrame genérico temporalmente para evitar errores antes de crear el MainFrame real

    /**
     * @param tutorService servicio al que se delegan las operaciones sobre tutores
     * @param mainFrame ventana padre usada para mostrar los diálogos de error y confirmación
     */
    public TutorController(TutorService tutorService, JFrame mainFrame) {
        this.tutorService = tutorService;
        this.mainFrame = mainFrame;
    }

    /**
     * @param nombre nombre del tutor
     * @param apellido apellido del tutor
     * @param email email de contacto del tutor
     * @param telefono teléfono de contacto del tutor
     */
    public void agregarTutor(String nombre, String apellido, String email, String telefono) {
        try {
            tutorService.agregarTutor(nombre, apellido, email, telefono);
            // Si funciona, el observer actualizará la tabla automáticamente
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar tutor", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param id id del tutor a modificar
     * @param nombre nuevo nombre del tutor
     * @param apellido nuevo apellido del tutor
     * @param email nuevo email del tutor
     * @param telefono nuevo teléfono del tutor
     */
    public void modificarTutor(String id, String nombre, String apellido, String email, String telefono) {
        try {
            tutorService.modificarTutor(id, nombre, apellido, email, telefono);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al modificar tutor", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pide confirmación al usuario antes de eliminar lógicamente al tutor.
     *
     * @param id id del tutor a eliminar
     */
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

    /**
     * @param tutorId id del tutor al que se agrega la materia
     * @param nombreMateria nombre de la nueva materia
     * @param tarifa tarifa por hora de la nueva materia
     * @param cupoMax cupo máximo de estudiantes de la nueva materia
     */
    public void agregarMateriaATutor(String tutorId, String nombreMateria, double tarifa, int cupoMax) {
        try {
            Materia materia = new Materia(nombreMateria, tarifa, cupoMax);
            tutorService.agregarMateriaATutor(tutorId, materia);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar materia", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param tutorId id del tutor al que se agrega el bloque
     * @param dia día de la semana del nuevo bloque
     * @param inicio hora de inicio del nuevo bloque
     * @param fin hora de fin del nuevo bloque
     */
    public void agregarBloqueATutor(String tutorId, diaSemana dia, LocalTime inicio, LocalTime fin) {
        try {
            BloqueHorario bloque = new BloqueHorario(dia, inicio, fin);
            tutorService.agregarBloqueATutor(tutorId, bloque);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error al agregar bloque", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return la lista de tutores activos
     */
    public List<Tutor> obtenerTodos() {
        return tutorService.obtenerTodos();
    }

    /**
     * @return la lista de nombres de materia sin repetir de todos los tutores activos
     */
    public List<String> obtenerMateriasUnicas() {
        return tutorService.obtenerMateriasUnicas();
    }

    /**
     * @param id id del tutor a buscar
     * @return el tutor con ese id, o null si ocurre un error de búsqueda
     */
    public Tutor buscarPorId(String id) {
        try {
            return tutorService.buscarPorId(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}