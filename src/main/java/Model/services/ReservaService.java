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


public class ReservaService implements observer {

    private List<Reserva> reservas;
    private List<modelObserver> observadores;


    public ReservaService() {
        this.reservas = new ArrayList<>();
        this.observadores = new ArrayList<>();

    }





    @Override
    public void agregarObservador(modelObserver o) {
        this.observadores.add(o);
    }

    @Override
    public void eliminarObservador(modelObserver o) {
        this.observadores.remove(o);

    }

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

    public Reserva buscarPorId(String id){
        for (Reserva r : reservas) {
            if (r.getId().equals(id)) {
                return r;
            }
        }

        throw new individuoNoEncontradoException("Reserva no encontrado");
    }

    public List<Reserva> obtenerTodas(){
        return new ArrayList<>(reservas);
    }

    public List<Reserva> obtenerReservasActivasPorTutor(String tutorId){

        List<Reserva> resultado = new ArrayList<>();

        for (Reserva r : reservas) {
            if(r.estaActiva() && r.getTutor().getId().equals(tutorId)){
                resultado.add(r);
            }
        }

        return resultado;
    }

    public List<Reserva> obtenerReservasActivasPorEstudiante(String estudianteId){
        List<Reserva> resultado = new ArrayList<>();

        for (Reserva r : reservas) {
            if(r.estaActiva() && r.getEstudiante().getId().equals(estudianteId)){
                resultado.add(r);
            }
        }

        return resultado;
    }

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

    public void cancelarReserva(String id){
        Reserva r = buscarPorId(id);
        r.cancelar();

        notifyObservador(eventoModelo.RESERVA_CANCELADA, r);

    }

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

    public void modificarReserva(String id, BloqueHorario nuevoBloque){

        Reserva r = buscarPorId(id);

        r.setBloqueHorario(nuevoBloque);

        notifyObservador(eventoModelo.RESERVA_MODIFICADO, r);

    }



}
