package Model.entidades;

import Model.Exceptions.datosInvalidosException;

public class Materia {
    private String nombre;
    private double tarifaPorHora;
    private int cupoMaximoEstudiantes;

    // Constructor con validaciones
    public Materia(String nombre, double tarifaPorHora, int cupoMaximoEstudiantes) {
        if (tarifaPorHora <= 0) {
            throw new datosInvalidosException("La tarifa por hora debe ser mayor a 0.");
        }
        if (cupoMaximoEstudiantes <= 0) {
            throw new datosInvalidosException("El cupo máximo debe ser mayor a 0.");
        }
        this.nombre = nombre;
        this.tarifaPorHora = tarifaPorHora;
        this.cupoMaximoEstudiantes = cupoMaximoEstudiantes;
    }

    // Getters y Setters con validación
    public String getNombre() { return nombre; }

    public double getTarifaPorHora() { return tarifaPorHora; }
    public void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora <= 0) {
            throw new datosInvalidosException("La tarifa por hora debe ser mayor a 0.");
        }
        this.tarifaPorHora = tarifaPorHora;
    }

    public int getCupoMaximoEstudiantes() { return cupoMaximoEstudiantes; }
    public void setCupoMaximoEstudiantes(int cupoMaximoEstudiantes) {
        if (cupoMaximoEstudiantes <= 0) {
            throw new datosInvalidosException("El cupo máximo debe ser mayor a 0.");
        }
        this.cupoMaximoEstudiantes = cupoMaximoEstudiantes;
    }

    @Override
    public String toString() {
        return nombre + " ($" + tarifaPorHora + "/hr, Max: " + cupoMaximoEstudiantes + " est.)";
    }
}