package Model.Strategy;

import Model.enums.diaSemana;

import java.time.LocalTime;

/**
 * Junta todos los posibles filtros que se pueden pedir al buscar tutores: materia, día,
 * hora de inicio, hora de fin y tarifa máxima. Se pasa completo a cada estrategia de
 * búsqueda, y cada una decide qué campos le importan.
 */
public class criterioDeBusqueda {

    String materia;
    diaSemana dia;
    LocalTime horaInicio;
    LocalTime horaFin;
    Double tarifaMaxima;

    /**
     * @param materia nombre de la materia buscada, o null si no aplica
     * @param dia día de la semana buscado, o null si no aplica
     * @param horaInicio hora de inicio del rango buscado, o null si no aplica
     * @param horaFin hora de fin del rango buscado, o null si no aplica
     * @param tarifaMaxima tarifa máxima aceptada, o null si no aplica
     */
    public criterioDeBusqueda(String materia, diaSemana dia, LocalTime horaInicio, LocalTime horaFin, Double tarifaMaxima) {
        this.materia = materia;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tarifaMaxima = tarifaMaxima;
    }

    /**
     * Crea un criterio de búsqueda vacío, sin ningún filtro asignado.
     */
    criterioDeBusqueda() {};

    /**
     * @return el nombre de la materia buscada, o null si no aplica
     */
    public String getMateria() {
        return materia;
    }

    /**
     * @return el día de la semana buscado, o null si no aplica
     */
    public diaSemana getDia() {
        return dia;
    }

    /**
     * @return la hora de inicio del rango buscado, o null si no aplica
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * @return la hora de fin del rango buscado, o null si no aplica
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * @return la tarifa máxima aceptada, o null si no aplica
     */
    public Double getTarifaMaxima() {
        return tarifaMaxima;
    }

    /**
     * @param materia nuevo nombre de materia a buscar
     */
    public void setMateria(String materia) {
        this.materia = materia;
    }

    /**
     * @param dia nuevo día de la semana a buscar
     */
    public void setDia(diaSemana dia) {
        this.dia = dia;
    }

    /**
     * @param horaInicio nueva hora de inicio del rango a buscar
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * @param horaFin nueva hora de fin del rango a buscar
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @param tarifaMaxima nueva tarifa máxima aceptada
     */
    public void setTarifaMaxima(Double tarifaMaxima) {
        this.tarifaMaxima = tarifaMaxima;
    }
}
