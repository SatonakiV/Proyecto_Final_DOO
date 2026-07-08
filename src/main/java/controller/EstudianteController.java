package controller;
import Model.entidades.Estudiante;
import Model.services.EstudianteService;

import java.util.List;

/**
 * Traduce las acciones de la Vista sobre estudiantes en llamadas a EstudianteService,
 * capturando las excepciones de negocio y devolviendo un mensaje de texto en vez de
 * dejarlas propagarse.
 */
public class EstudianteController {

    private EstudianteService estudianteService;

    /**
     * @param estudianteService servicio al que se delegan las operaciones sobre estudiantes
     */
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    /**
     * @param nombre nombre del estudiante
     * @param apellido apellido del estudiante
     * @param email email de contacto del estudiante
     * @param telefono teléfono de contacto del estudiante
     * @return un mensaje de éxito, o un mensaje de error si la operación falla
     */
    public String registrar(String nombre, String apellido, String email, String telefono) {
        try {
            estudianteService.agregarEstudiante(nombre, apellido, email, telefono);
            return "Estudiante registrado con éxito.";
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return "Error al registrar";
        }
    }

    /**
     * @param id id del estudiante a modificar
     * @param nombre nuevo nombre del estudiante
     * @param apellido nuevo apellido del estudiante
     * @param email nuevo email del estudiante
     * @param telefono nuevo teléfono del estudiante
     * @return un mensaje de éxito, o un mensaje de error si la operación falla
     */
    public String modificar(String id, String nombre, String apellido, String email, String telefono) {
        try {
            estudianteService.modificarEstudiante(id, nombre, apellido, email, telefono);
            return "Estudiante modificado con éxito.";
        } catch (Exception e) {
            return "Error al modificar: " + e.getMessage();
        }
    }

    /**
     * @param id id del estudiante a eliminar
     * @return un mensaje de éxito, o un mensaje de error si la operación falla
     */
    public String eliminar(String id) {
        try {
            estudianteService.eliminarEstudiante(id);
            return "Estudiante eliminado con éxito.";
        } catch (Exception e) {
            return "Error al eliminar: " + e.getMessage();
        }
    }

    /**
     * @return la lista de estudiantes activos
     */
    public List<Estudiante> obtenerTodos() {
        return estudianteService.obtenerTodos();
    }

    /**
     * @param id id del estudiante a buscar
     * @return el estudiante con ese id
     * @throws Model.Exceptions.individuoNoEncontradoException si no existe ningún estudiante con ese id
     */
    public Model.entidades.Estudiante buscarPorId(String id) {
        return estudianteService.buscarPorId(id);
    }
}