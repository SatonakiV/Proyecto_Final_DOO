package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import Model.enums.estadoReserva;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reserva {

    private String id;
    private Tutor tutor;
    private Estudiante estudiante;
    private Materia materia;
    private BloqueHorario bloqueHorario;
    private LocalDateTime fechaCreacion;
    private estadoReserva estado;
    private String notas;

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

    public void cancelar(){
        this.estado = Model.enums.estadoReserva.CANCELADA;
    }

    public void completar(){
        this.estado = Model.enums.estadoReserva.COMPLETADA;
    }

    public boolean estaActiva(){
        return estado == Model.enums.estadoReserva.ACTIVA;
    }

    public boolean conflictaCon(Reserva otra) {
        return bloqueHorario.seSolapaCon(otra.getBloqueHorario());
    }

    public String getId() {
        return id;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Materia getMateria() {
        return materia;
    }

    public BloqueHorario getBloqueHorario() {
        return bloqueHorario;
    }

    public void setBloqueHorario(BloqueHorario bloqueHorario) {
        this.bloqueHorario = bloqueHorario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public estadoReserva getEstado() {
        return estado;
    }

    public String getNotas() {
        return notas;
    }

    @Override
    public String toString() {
        return "Reserva[" + id + " | " + "Tutor: " + tutor.getNombre() + " | " + "Estudiante: " + estudiante.getNombre() +
        " | " + "Materia: " + materia.getNombre() + " | " + bloqueHorario +  " | " + estado.getDescripcion() + "]";
    }
}
