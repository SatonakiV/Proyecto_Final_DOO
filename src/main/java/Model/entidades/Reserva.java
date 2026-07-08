package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import Model.enums.estadoReserva;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Une a un tutor, un estudiante, una materia y un bloque de horario en una clase agendada.
 * Una reserva nunca se borra: se cancela o se completa, cambiando su estado, para no
 * perder el historial.
 */
public class Reserva {

    private String id;
    private Tutor tutor;
    private Estudiante estudiante;
    private Materia materia;
    private BloqueHorario bloqueHorario;
    private LocalDateTime fechaCreacion;
    private estadoReserva estado;
    private String notas;

    /**
     * Crea una reserva nueva en estado ACTIVA, con un id generado automáticamente y la
     * fecha de creación fijada al momento actual.
     *
     * @param tutor tutor de la reserva
     * @param estudiante estudiante de la reserva
     * @param materia materia de la reserva
     * @param bloqueHorario bloque de horario de la reserva
     * @param notas notas adicionales de la reserva
     * @throws datosInvalidosException si el tutor, el estudiante, la materia o el bloque de horario son null
     */
    public Reserva(Tutor tutor, Estudiante estudiante, Materia materia, BloqueHorario bloqueHorario, String notas) {
        if(tutor == null || estudiante == null || materia == null || bloqueHorario == null) {
            throw new datosInvalidosException("Por favor ingrese datos validos");
        }

        this.tutor = tutor;
        this.estudiante = estudiante;
        this.materia = materia;
        this.bloqueHorario = bloqueHorario;
        this.notas = notas;
        this.id = UUID.randomUUID().toString();
        this.fechaCreacion = LocalDateTime.now();
        this.estado = Model.enums.estadoReserva.ACTIVA;



    }

    /**
     * Cambia el estado de la reserva a CANCELADA.
     */
    public void cancelar(){
        this.estado = Model.enums.estadoReserva.CANCELADA;
    }

    /**
     * Cambia el estado de la reserva a COMPLETADA.
     */
    public void completar(){
        this.estado = Model.enums.estadoReserva.COMPLETADA;
    }

    /**
     * @return true si el estado de la reserva es ACTIVA, false en caso contrario
     */
    public boolean estaActiva(){
        return estado == Model.enums.estadoReserva.ACTIVA;
    }

    /**
     * @param otra otra reserva contra la cual comparar
     * @return true si los bloques de horario de ambas reservas se solapan, false en caso contrario
     */
    public boolean conflictaCon(Reserva otra) {
        return bloqueHorario.seSolapaCon(otra.getBloqueHorario());
    }

    /**
     * @return el id único de la reserva
     */
    public String getId() {
        return id;
    }

    /**
     * @return el tutor de la reserva
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * @return el estudiante de la reserva
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @return la materia de la reserva
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * @return el bloque de horario de la reserva
     */
    public BloqueHorario getBloqueHorario() {
        return bloqueHorario;
    }

    /**
     * @param bloqueHorario nuevo bloque de horario de la reserva
     */
    public void setBloqueHorario(BloqueHorario bloqueHorario) {
        this.bloqueHorario = bloqueHorario;
    }

    /**
     * @return la fecha y hora en que se creó la reserva
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @return el estado actual de la reserva
     */
    public estadoReserva getEstado() {
        return estado;
    }

    /**
     * @return las notas adicionales de la reserva
     */
    public String getNotas() {
        return notas;
    }

    /**
     * @return una representación en texto de la reserva con sus datos principales
     */
    @Override
    public String toString() {
        return "Reserva[" + id + " | " + "Tutor: " + tutor.getNombre() + " | " + "Estudiante: " + estudiante.getNombre() +
        " | " + "Materia: " + materia.getNombre() + " | " + bloqueHorario +  " | " + estado.getDescripcion() + "]";
    }
}
