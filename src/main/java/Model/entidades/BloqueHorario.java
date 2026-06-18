package Model.entidades;

import Model.enums.diaSemana;
import Model.Exceptions.datosInvalidosException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class BloqueHorario {
    private diaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Constructor que recibe día, inicio y fin, con validación [cite: 348, 349]
    public BloqueHorario(diaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new datosInvalidosException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Retorna true si ambos bloques están en el mismo día y sus rangos horarios se solapan [cite: 351, 352, 353]
    public boolean seSolapaCon(BloqueHorario otro) {
        if (this.dia != otro.getDia()) {
            return false;
        }
        // Lógica de solapamiento: (InicioA < FinB) y (FinA > InicioB)
        return this.horaInicio.isBefore(otro.getHoraFin()) && this.horaFin.isAfter(otro.getHoraInicio());
    }

    // Retorna true si la hora está dentro del rango
    public boolean contiene(LocalTime hora) {
        return !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
    }

    // Calcula la duración en minutos
    public long getDuracionMinutos() {
        return ChronoUnit.MINUTES.between(horaInicio, horaFin);
    }

    // Getters
    public diaSemana getDia() { return dia; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }

    // Representación textual legible
    @Override
    public String toString() {
        return dia.toString() + " " + horaInicio.toString() + " - " + horaFin.toString();
    }
}