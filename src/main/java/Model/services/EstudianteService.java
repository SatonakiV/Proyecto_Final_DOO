package Model.services;

import Model.Exceptions.individuoNoEncontradoException;
import Model.entidades.Estudiante;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import Model.observer.observer;

import java.util.ArrayList;
import java.util.List;


/**
 * Guarda la lista de estudiantes en memoria y aplica las reglas de negocio para
 * agregarlos, modificarlos y eliminarlos lógicamente. Notifica cada cambio a sus
 * observadores.
 */
public class EstudianteService implements observer {

    private List<Estudiante> estudiantes;
    private List<modelObserver> observadores;


    public EstudianteService(){
        estudiantes = new ArrayList<>();
        observadores = new ArrayList<>();
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

            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * @param id id del estudiante a buscar
     * @return el estudiante con ese id
     * @throws individuoNoEncontradoException si no existe ningún estudiante con ese id
     */
    public Estudiante buscarPorId(String id) {
        for(Estudiante e: estudiantes) {

            if (e.getId().equals(id)) {
                return e;
            }

        }
        throw new individuoNoEncontradoException("No existe el estudiante con el id " + id);
    }


    /**
     * @return la lista de estudiantes activos (los eliminados lógicamente quedan ocultos)
     */
    public List<Estudiante> obtenerTodos(){

        List<Estudiante> estudiantesActivos = new ArrayList<>();

        for(Estudiante e: estudiantes){
            if(e.isActivo() == true){
                estudiantesActivos.add(e);
            }

        }

        return estudiantesActivos;
    }




    /**
     * Crea y agrega un nuevo estudiante activo a la lista, notificando el evento correspondiente.
     *
     * @param nombre nombre del estudiante
     * @param apellido apellido del estudiante
     * @param email email de contacto del estudiante
     * @param telefono teléfono de contacto del estudiante
     * @throws Model.Exceptions.datosInvalidosException si faltan nombre, apellido o email, o si el email ya está en uso por otro estudiante activo
     */
    public void agregarEstudiante(String nombre, String apellido, String email, String telefono){
        validarDatos(nombre, apellido, email);

        // Validar duplicado por email
        for (Estudiante e : estudiantes) {
            if (e.isActivo() && e.getEmail().equalsIgnoreCase(email.trim())) {
                throw new Model.Exceptions.datosInvalidosException("Ya existe un estudiante con el correo: " + email);
            }
        }

        Estudiante nuevoEstudiante = new Estudiante(nombre.trim(), apellido.trim(), email.trim(), telefono != null ? telefono.trim() : "");
        this.estudiantes.add(nuevoEstudiante);
        notifyObservador(eventoModelo.ESTUDIANTE_AGREGADO, nuevoEstudiante);
    }

    /**
     * Modifica los datos de un estudiante existente y notifica el evento correspondiente.
     *
     * @param id id del estudiante a modificar
     * @param nombre nuevo nombre del estudiante
     * @param apellido nuevo apellido del estudiante
     * @param email nuevo email del estudiante
     * @param telefono nuevo teléfono del estudiante
     * @throws Model.Exceptions.datosInvalidosException si faltan nombre, apellido o email, o si el email ya está en uso por otro estudiante activo
     * @throws individuoNoEncontradoException si no existe ningún estudiante con ese id
     */
    public void modificarEstudiante(String id, String nombre, String apellido, String email, String telefono){
        validarDatos(nombre, apellido, email);
        
        // Validar duplicado excluyendo al estudiante actual
        for (Estudiante e : estudiantes) {
            if (e.isActivo() && !e.getId().equals(id) && e.getEmail().equalsIgnoreCase(email.trim())) {
                throw new Model.Exceptions.datosInvalidosException("Ya existe OTRO estudiante con el correo: " + email);
            }
        }

        Estudiante e = buscarPorId(id);
        e.setNombre(nombre.trim());
        e.setApellido(apellido.trim());
        e.setEmail(email.trim());
        e.setTelefono(telefono != null ? telefono.trim() : "");

        notifyObservador(eventoModelo.ESTUDIANTE_MODIFICADO, e);
    }

    /**
     * @param nombre nombre a validar
     * @param apellido apellido a validar
     * @param email email a validar
     * @throws Model.Exceptions.datosInvalidosException si el nombre, el apellido o el email son null o están vacíos
     */
    private void validarDatos(String nombre, String apellido, String email) {
        if (nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            throw new Model.Exceptions.datosInvalidosException("Nombre, apellido y correo electrónico son obligatorios.");
        }
    }

    /**
     * Elimina lógicamente a un estudiante (marca su campo activo en false) y notifica el evento correspondiente.
     *
     * @param id id del estudiante a eliminar
     * @throws individuoNoEncontradoException si no existe ningún estudiante con ese id
     */
    public void eliminarEstudiante(String id){

        Estudiante e = buscarPorId(id);

        e.setActivo(false);

        notifyObservador(eventoModelo.ESTUDIANTE_ELIMINADO, e);
    }




}
