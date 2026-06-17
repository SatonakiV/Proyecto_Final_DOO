package Model.Strategy;

import Model.enums.diaSemana;

import java.time.LocalTime;

public class criterioDeBusqueda {

    String materia;
    diaSemana dia;
    LocalTime horaInicio;
    LocalTime horaFin;
    Double tarifaMaxima;

    public criterioDeBusqueda(String materia, diaSemana dia, LocalTime horaInicio, LocalTime horaFin, Double tarifaMaxima) {
        this.materia = materia;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tarifaMaxima = tarifaMaxima;
    }
    criterioDeBusqueda() {};

    public String getMateria() {
        return materia;
    }

    public diaSemana getDia() {
        return dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public Double getTarifaMaxima() {
        return tarifaMaxima;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setDia(diaSemana dia) {
        this.dia = dia;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setTarifaMaxima(Double tarifaMaxima) {
        this.tarifaMaxima = tarifaMaxima;
    }
}
