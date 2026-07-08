package Model.entidades;

import Model.Exceptions.datosInvalidosException;

/**
 * Representa una materia que puede dictar un tutor, con su tarifa por hora y el cupo
 * máximo de estudiantes que puede tener inscritos en un mismo bloque de horario.
 */
public class Materia {
    private String nombre;
    private double tarifaPorHora;
    private int cupoMaximoEstudiantes;

    /**
     * @param nombre nombre de la materia
     * @param tarifaPorHora tarifa por hora de la materia
     * @param cupoMaximoEstudiantes cupo máximo de estudiantes por bloque de horario
     * @throws datosInvalidosException si la tarifa o el cupo máximo no son mayores a 0
     */
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

    /**
     * @return el nombre de la materia
     */
    public String getNombre() { return nombre; }

    /**
     * @return la tarifa por hora de la materia
     */
    public double getTarifaPorHora() { return tarifaPorHora; }
    /**
     * @param tarifaPorHora nueva tarifa por hora de la materia
     * @throws datosInvalidosException si la tarifa no es mayor a 0
     */
    public void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora <= 0) {
            throw new datosInvalidosException("La tarifa por hora debe ser mayor a 0.");
        }
        this.tarifaPorHora = tarifaPorHora;
    }

    /**
     * @return el cupo máximo de estudiantes de la materia
     */
    public int getCupoMaximoEstudiantes() { return cupoMaximoEstudiantes; }
    /**
     * @param cupoMaximoEstudiantes nuevo cupo máximo de estudiantes de la materia
     * @throws datosInvalidosException si el cupo máximo no es mayor a 0
     */
    public void setCupoMaximoEstudiantes(int cupoMaximoEstudiantes) {
        if (cupoMaximoEstudiantes <= 0) {
            throw new datosInvalidosException("El cupo máximo debe ser mayor a 0.");
        }
        this.cupoMaximoEstudiantes = cupoMaximoEstudiantes;
    }

    /**
     * @return una representación en texto de la materia con su tarifa y cupo máximo
     */
    @Override
    public String toString() {
        return nombre + " ($" + tarifaPorHora + "/hr, Max: " + cupoMaximoEstudiantes + " est.)";
    }
}