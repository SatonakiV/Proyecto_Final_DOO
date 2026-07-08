package Model.services;

import Model.Exceptions.conflictoHorarioException;
import Model.Exceptions.cupoMaximoException;
import Model.Exceptions.individuoNoEncontradoException;
import Model.Exceptions.tutorNoDisponibleException;
import Model.entidades.*;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import Model.observer.observer;

import java.util.ArrayList;
import java.util.List;


/**
 * Guarda la lista de reservas en memoria y aplica las reglas de negocio para agendar,
 * cancelar y modificar clases. Es el servicio con más reglas, porque agendar una clase
 * depende de la disponibilidad del tutor, los choques de horario del estudiante y el
 * cupo máximo de la materia a la vez.
 */
public class ReservaService implements observer {

    private List<Reserva> reservas;
    private List<modelObserver> observadores;


    public ReservaService() {
        this.reservas = new ArrayList<>();
        this.observadores = new ArrayList<>();

    }





    /**
     * @param o observador a registrar
     */
    @Override
    public void agregarObservador(modelObserver o) {
        this.observadores.add(o);
    }

    /**
     * @param o observador a quitar del registro
     */
    @Override
    public void eliminarObservador(modelObserver o) {
        this.observadores.remove(o);

    }

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void notifyObservador(eventoModelo evento, Object datos) {
        for (modelObserver o : observadores) {

            try{
                o.onModeloActualizado(evento, datos);
            }

            catch(Exception e){
                System.out.println("Error al notificar observadores");
            }
        }
    }

    /**
     * @param id id de la reserva a buscar
     * @return la reserva con ese id
     * @throws individuoNoEncontradoException si no existe ninguna reserva con ese id
     */
    public Reserva buscarPorId(String id){
        for (Reserva r : reservas) {
            if (r.getId().equals(id)) {
                return r;
            }
        }

        throw new individuoNoEncontradoException("Reserva no encontrado");
    }

    /**
     * @return una copia de la lista de todas las reservas, activas o no
     */
    public List<Reserva> obtenerTodas(){
        return new ArrayList<>(reservas);
    }

    /**
     * @param tutorId id del tutor cuyas reservas activas se quieren obtener
     * @return la lista de reservas activas de ese tutor
     */
    public List<Reserva> obtenerReservasActivasPorTutor(String tutorId){

        List<Reserva> resultado = new ArrayList<>();

        for (Reserva r : reservas) {
            if(r.estaActiva() && r.getTutor().getId().equals(tutorId)){
                resultado.add(r);
            }
        }

        return resultado;
    }

    /**
     * @param estudianteId id del estudiante cuyas reservas activas se quieren obtener
     * @return la lista de reservas activas de ese estudiante
     */
    public List<Reserva> obtenerReservasActivasPorEstudiante(String estudianteId){
        List<Reserva> resultado = new ArrayList<>();

        for (Reserva r : reservas) {
            if(r.estaActiva() && r.getEstudiante().getId().equals(estudianteId)){
                resultado.add(r);
            }
        }

        return resultado;
    }

    /**
     * @param tutorId id del tutor a revisar
     * @param nombreMateria nombre de la materia a revisar
     * @param bloque bloque de horario a revisar
     * @return la cantidad de reservas activas de ese tutor y materia que se solapan con el bloque dado
     */
    public int contarEstudianteEnBloque(String tutorId, String nombreMateria, BloqueHorario bloque){
        int contador = 0;
        for (Reserva r : reservas) {
            if(r.estaActiva() &&
            r.getTutor().getId().equals(tutorId) &&
            r.getMateria().getNombre().equals(nombreMateria) &&
            r.getBloqueHorario().seSolapaCon(bloque)){
                contador++;
            }
        }

        return contador;
    }

    /**
     * Cancela una reserva existente y notifica el evento correspondiente.
     *
     * @param id id de la reserva a cancelar
     * @throws individuoNoEncontradoException si no existe ninguna reserva con ese id
     */
    public void cancelarReserva(String id){
        Reserva r = buscarPorId(id);
        r.cancelar();

        notifyObservador(eventoModelo.RESERVA_CANCELADA, r);

    }

    /**
     * Crea una nueva reserva, validando en orden que el tutor trabaje en ese horario,
     * que el estudiante no tenga otra clase que choque en el tiempo, y que no se haya
     * llegado al cupo máximo de la materia en ese bloque. Notifica el evento correspondiente.
     *
     * @param tutor tutor de la reserva
     * @param estudiante estudiante de la reserva
     * @param materia materia de la reserva
     * @param bloque bloque de horario de la reserva
     * @param notas notas adicionales de la reserva
     * @throws tutorNoDisponibleException si el tutor no tiene disponibilidad en ese horario
     * @throws conflictoHorarioException si el estudiante ya tiene una clase que choca con ese horario
     * @throws cupoMaximoException si ya se alcanzó el cupo máximo de la materia en ese bloque
     */
    public void crearReserva(Tutor tutor, Estudiante estudiante, Materia materia, BloqueHorario bloque, String notas){

        if(!tutor.tieneDisponibilidadEn(bloque.getDia(), bloque.getHoraInicio(), bloque.getHoraFin())){
            throw new tutorNoDisponibleException("El tutor no trabaja en ese horario");
        }

        List<Reserva> clasesEstudiante = obtenerReservasActivasPorEstudiante(estudiante.getId());

        for(Reserva r : clasesEstudiante){
            if(r.getBloqueHorario().seSolapaCon(bloque)){
                throw new conflictoHorarioException("El estudiante ya tiene una clase que choca con este horario");
            }
        }

        int inscritos = contarEstudianteEnBloque(tutor.getId(), materia.getNombre(), bloque);

        if(inscritos >= materia.getCupoMaximoEstudiantes()){
            throw new cupoMaximoException("El tutor ya alcanzo el límite de estudiantes para la materia en este horario");
        }

        Reserva nuevaReserva = new Reserva(tutor, estudiante, materia, bloque, notas);
        reservas.add(nuevaReserva);

        notifyObservador(eventoModelo.RESERVA_CREADA,nuevaReserva);
    }

    /**
     * Cambia el bloque de horario de una reserva existente y notifica el evento correspondiente.
     *
     * @param id id de la reserva a modificar
     * @param nuevoBloque nuevo bloque de horario de la reserva
     * @throws individuoNoEncontradoException si no existe ninguna reserva con ese id
     */
    public void modificarReserva(String id, BloqueHorario nuevoBloque){

        Reserva r = buscarPorId(id);

        r.setBloqueHorario(nuevoBloque);

        notifyObservador(eventoModelo.RESERVA_MODIFICADO, r);

    }



}
