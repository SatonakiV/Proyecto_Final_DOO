package Model.entidades;

import Model.enums.diaSemana;
import Model.Exceptions.datosInvalidosException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Representa un rango de tiempo dentro de un día de la semana, usado tanto para la
 * disponibilidad de un tutor como para el horario de una reserva.
 */
public class BloqueHorario {
    private diaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    /**
     * @param dia día de la semana del bloque
     * @param horaInicio hora de inicio del bloque
     * @param horaFin hora de fin del bloque
     * @throws datosInvalidosException si la hora de fin no es posterior a la hora de inicio
     */
    public BloqueHorario(diaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new datosInvalidosException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * @param otro bloque de horario contra el cual comparar
     * @return true si ambos bloques están en el mismo día y sus rangos horarios se cruzan, false en caso contrario
     */
    public boolean seSolapaCon(BloqueHorario otro) {
        if (this.dia != otro.getDia()) {
            return false;
        }
        // Lógica de solapamiento: (InicioA < FinB) y (FinA > InicioB)
        return this.horaInicio.isBefore(otro.getHoraFin()) && this.horaFin.isAfter(otro.getHoraInicio());
    }

    /**
     * @param hora hora a revisar
     * @return true si la hora está dentro del rango del bloque, false en caso contrario
     */
    public boolean contiene(LocalTime hora) {
        return !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
    }

    /**
     * @return la duración del bloque en minutos
     */
    public long getDuracionMinutos() {
        return ChronoUnit.MINUTES.between(horaInicio, horaFin);
    }

    /**
     * @return el día de la semana del bloque
     */
    public diaSemana getDia() { return dia; }
    /**
     * @return la hora de inicio del bloque
     */
    public LocalTime getHoraInicio() { return horaInicio; }
    /**
     * @return la hora de fin del bloque
     */
    public LocalTime getHoraFin() { return horaFin; }

    /**
     * @return una representación en texto del bloque con su día y rango horario
     */
    @Override
    public String toString() {
        return dia.toString() + " " + horaInicio.toString() + " - " + horaFin.toString();
    }
}