package Model.services;

import Model.entidades.Tutor;
import Model.entidades.Materia;
import Model.entidades.BloqueHorario;
import Model.enums.eventoModelo;
import Model.Exceptions.datosInvalidosException;
import Model.Exceptions.individuoNoEncontradoException;
import Model.observer.modelObserver;
import Model.observer.observer; // Interfaz Observable según tu captura

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TutorService implements observer {
    private List<Tutor> tutores;
    private List<modelObserver> observadores;

    public TutorService() {
        this.tutores = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public void agregarTutor(String nombre, String apellido, String email, String telefono) {
        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new datosInvalidosException("El nombre y el apellido no pueden estar vacíos.");
        }
        Tutor nuevoTutor = new Tutor(nombre, apellido, email, telefono);
        tutores.add(nuevoTutor);
        notifyObservador(eventoModelo.TUTOR_AGREGADO, nuevoTutor);
    }

    public void modificarTutor(String id, String nombre, String apellido, String email, String telefono) {
        Tutor tutor = buscarPorId(id);

        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new datosInvalidosException("El nombre y el apellido no pueden estar vacíos.");
        }


        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    public void eliminarTutor(String id) {
        Tutor tutor = buscarPorId(id);
        tutor.setActivo(false); // Borrado lógico
        notifyObservador(eventoModelo.TUTOR_ELIMINADO, tutor);
    }

    public Tutor buscarPorId(String id) {
        for (Tutor t : tutores) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        throw new individuoNoEncontradoException("No se encontró ningún tutor con el ID especificado.");
    }

    public List<Tutor> obtenerTodos() {
        // Retorna solo los tutores activos
        return tutores.stream()
                .filter(Tutor::isActivo)
                .collect(Collectors.toUnmodifiableList());
    }

    public void agregarMateriaATutor(String tutorId, Materia materia) {
        Tutor tutor = buscarPorId(tutorId);
        tutor.agregarMateria(materia);
        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    public void agregarBloqueATutor(String tutorId, BloqueHorario bloque) {
        Tutor tutor = buscarPorId(tutorId);
        tutor.agregarBloqueDisponibilidad(bloque);
        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    // --- Implementación del patrón Observer ---

    @Override
    public void agregarObservador(modelObserver observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(modelObserver observador) {
        observadores.remove(observador);
    }

    @Override
    public void notifyObservador(eventoModelo evento, Object datos) {
        for (modelObserver obs : observadores) {
            obs.onModeloActualizado(evento, datos);
        }
    }
}