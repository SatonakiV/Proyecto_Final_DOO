package Model.entidades;

import Model.Exceptions.datosInvalidosException;

import java.util.UUID;

/**
 * Representa a un estudiante del sistema, con sus datos de contacto y su estado de actividad.
 * Un estudiante "eliminado" no se borra realmente: se marca como inactivo para no perder
 * el historial de reservas asociadas a él.
 */
public class Estudiante {

    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean activo;

    /**
     * Crea un estudiante nuevo, activo por defecto, con un id generado automáticamente.
     *
     * @param nombre nombre del estudiante
     * @param apellido apellido del estudiante
     * @param email email de contacto del estudiante
     * @param telefono teléfono de contacto del estudiante
     * @throws datosInvalidosException si alguno de los parámetros es null
     */
    public Estudiante(String nombre, String apellido, String email, String telefono){
        if(nombre == null || apellido == null || email == null || telefono == null){
            throw new datosInvalidosException("Datos incompletos");

        }

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.id = UUID.randomUUID().toString();
        this.activo = true;
    }

    /**
     * @return el nombre y apellido del estudiante concatenados en un solo texto
     */
    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    /**
     * @return el id único del estudiante
     */
    public String getId() {
        return id;
    }

    /**
     * @return el nombre del estudiante
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return el apellido del estudiante
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @return el email del estudiante
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return el teléfono del estudiante
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @return true si el estudiante está activo, false si fue eliminado lógicamente
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param nombre nuevo nombre del estudiante
     * @throws datosInvalidosException si el nombre es null
     */
    public void setNombre(String nombre) {
        if (nombre == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.nombre = nombre;
    }

    /**
     * @param apellido nuevo apellido del estudiante
     * @throws datosInvalidosException si el apellido es null
     */
    public void setApellido(String apellido) {
        if (apellido == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.apellido = apellido;
    }

    /**
     * @param email nuevo email del estudiante
     * @throws datosInvalidosException si el email es null
     */
    public void setEmail(String email) {
        if (email == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.email = email;
    }

    /**
     * @param telefono nuevo teléfono del estudiante
     * @throws datosInvalidosException si el teléfono es null
     */
    public void setTelefono(String telefono) {
        if (telefono == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.telefono = telefono;
    }

    /**
     * @param activo nuevo estado de actividad del estudiante
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return una representación en texto del estudiante con su nombre y apellido
     */
    @Override
    public String toString(){
        return "Estudiante: " + nombre + " " + apellido;
    }
}


