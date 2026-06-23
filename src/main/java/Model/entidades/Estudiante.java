package Model.entidades;

import Model.Exceptions.datosInvalidosException;

import java.util.UUID;

public class Estudiante {

    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean activo;

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

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        if (nombre == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        if (apellido == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.apellido = apellido;
    }

    public void setEmail(String email) {
        if (email == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.email = email;
    }

    public void setTelefono(String telefono) {
        if (telefono == null){
            throw new datosInvalidosException("Datos incompletos");
        }

        this.telefono = telefono;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString(){
        return "Estudiante: " + nombre + apellido;
    }
}


