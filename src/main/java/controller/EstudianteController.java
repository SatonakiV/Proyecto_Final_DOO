package controller;
import Model.services.EstudianteService;

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
}