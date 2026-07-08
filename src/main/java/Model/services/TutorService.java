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

/**
 * Guarda la lista de tutores en memoria y aplica las reglas de negocio para
 * agregarlos, modificarlos, eliminarlos lógicamente y gestionar sus materias y bloques
 * de horario. Notifica cada cambio a sus observadores.
 */
public class TutorService implements observer {
    private List<Tutor> tutores;
    private List<modelObserver> observadores;

    public TutorService() {
        this.tutores = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    /**
     * Crea y agrega un nuevo tutor activo a la lista, notificando el evento correspondiente.
     *
     * @param nombre nombre del tutor
     * @param apellido apellido del tutor
     * @param email email de contacto del tutor
     * @param telefono teléfono de contacto del tutor
     * @throws datosInvalidosException si el nombre o el apellido están vacíos
     */
    public void agregarTutor(String nombre, String apellido, String email, String telefono) {
        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new datosInvalidosException("El nombre y el apellido no pueden estar vacíos.");
        }
        Tutor nuevoTutor = new Tutor(nombre, apellido, email, telefono);
        tutores.add(nuevoTutor);
        notifyObservador(eventoModelo.TUTOR_AGREGADO, nuevoTutor);
    }

    /**
     * Modifica los datos de un tutor existente y notifica el evento correspondiente.
     *
     * @param id id del tutor a modificar
     * @param nombre nuevo nombre del tutor
     * @param apellido nuevo apellido del tutor
     * @param email nuevo email del tutor
     * @param telefono nuevo teléfono del tutor
     * @throws individuoNoEncontradoException si no existe ningún tutor con ese id
     * @throws datosInvalidosException si el nombre o el apellido están vacíos
     */
    public void modificarTutor(String id, String nombre, String apellido, String email, String telefono) {
        Tutor tutor = buscarPorId(id);

        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new datosInvalidosException("El nombre y el apellido no pueden estar vacíos.");
        }

        tutor.setNombre(nombre);
        tutor.setApellido(apellido);
        tutor.setEmail(email);
        tutor.setTelefono(telefono);

        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    /**
     * Elimina lógicamente a un tutor (marca su campo activo en false) y notifica el evento correspondiente.
     *
     * @param id id del tutor a eliminar
     * @throws individuoNoEncontradoException si no existe ningún tutor con ese id
     */
    public void eliminarTutor(String id) {
        Tutor tutor = buscarPorId(id);
        tutor.setActivo(false); // Borrado lógico
        notifyObservador(eventoModelo.TUTOR_ELIMINADO, tutor);
    }

    /**
     * @param id id del tutor a buscar
     * @return el tutor con ese id
     * @throws individuoNoEncontradoException si no existe ningún tutor con ese id
     */
    public Tutor buscarPorId(String id) {
        for (Tutor t : tutores) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        throw new individuoNoEncontradoException("No se encontró ningún tutor con el ID especificado.");
    }

    /**
     * @return la lista de tutores activos (los eliminados lógicamente quedan ocultos)
     */
    public List<Tutor> obtenerTodos() {
        // Retorna solo los tutores activos
        return tutores.stream()
                .filter(Tutor::isActivo)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return la lista de nombres de materia sin repetir, recorriendo las materias de todos los tutores activos
     */
    public List<String> obtenerMateriasUnicas() {
        List<String> materias = new ArrayList<>();
        for (Tutor t : obtenerTodos()) {
            for (Materia m : t.getMaterias()) {
                if (!materias.contains(m.getNombre())) {
                    materias.add(m.getNombre());
                }
            }
        }
        return materias;
    }

    /**
     * Agrega una materia a un tutor existente y notifica el evento correspondiente.
     *
     * @param tutorId id del tutor al que se agrega la materia
     * @param materia materia a agregar
     * @throws individuoNoEncontradoException si no existe ningún tutor con ese id
     * @throws datosInvalidosException si el tutor ya dicta una materia con el mismo nombre
     */
    public void agregarMateriaATutor(String tutorId, Materia materia) {
        Tutor tutor = buscarPorId(tutorId);
        tutor.agregarMateria(materia);
        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    /**
     * Agrega un bloque de disponibilidad horaria a un tutor existente y notifica el evento correspondiente.
     *
     * @param tutorId id del tutor al que se agrega el bloque
     * @param bloque bloque de horario a agregar
     * @throws individuoNoEncontradoException si no existe ningún tutor con ese id
     * @throws datosInvalidosException si el nuevo bloque se solapa con uno ya existente
     */
    public void agregarBloqueATutor(String tutorId, BloqueHorario bloque) {
        Tutor tutor = buscarPorId(tutorId);
        tutor.agregarBloqueDisponibilidad(bloque);
        notifyObservador(eventoModelo.TUTOR_MODIFICADO, tutor);
    }

    // --- Implementación del patrón Observer ---

    /**
     * @param observador observador a registrar
     */
    @Override
    public void agregarObservador(modelObserver observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     * @param observador observador a quitar del registro
     */
    @Override
    public void eliminarObservador(modelObserver observador) {
        observadores.remove(observador);
    }

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void notifyObservador(eventoModelo evento, Object datos) {
        for (modelObserver obs : observadores) {
            try {
                obs.onModeloActualizado(evento, datos);
            } catch (Exception e) {
                System.out.println("Error al notificar observadores de TutorService: " + e.getMessage());
            }
        }
    }
}