package controller;
import Model.entidades.Estudiante;
import Model.services.EstudianteService;

import java.util.List;

public class EstudianteController {

    private EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public String registrar(String nombre, String apellido, String email, String telefono) {
        try {
            estudianteService.agregarEstudiante(nombre, apellido, email, telefono);
            return "Estudiante registrado con éxito.";
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return "Error al registrar";
        }
    }

    public String modificar(String id, String nombre, String apellido, String email, String telefono) {
        try {
            estudianteService.modificarEstudiante(id, nombre, apellido, email, telefono);
            return "Estudiante modificado con éxito.";
        } catch (Exception e) {
            return "Error al modificar: " + e.getMessage();
        }
    }

    public String eliminar(String id) {
        try {
            estudianteService.eliminarEstudiante(id);
            return "Estudiante eliminado con éxito.";
        } catch (Exception e) {
            return "Error al eliminar: " + e.getMessage();
        }
    }

    public List<Estudiante> obtenerTodos() {
        return estudianteService.obtenerTodos();
    }

    public Model.entidades.Estudiante buscarPorId(String id) {
        return estudianteService.buscarPorId(id);
    }
}