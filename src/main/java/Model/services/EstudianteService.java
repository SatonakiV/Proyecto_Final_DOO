package Model.services;

import Model.Exceptions.individuoNoEncontradoException;
import Model.entidades.Estudiante;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import Model.observer.observer;

import java.util.ArrayList;
import java.util.List;


public class EstudianteService implements observer {

    private List<Estudiante> estudiantes;
    private List<modelObserver> observadores;


    public EstudianteService(){
        estudiantes = new ArrayList<>();
        observadores = new ArrayList<>();
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

            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    public Estudiante buscarPorId(String id) {
        for(Estudiante e: estudiantes) {

            if (e.getId().equals(id)) {
                return e;
            }

        }
        throw new individuoNoEncontradoException("No existe el estudiante con el id " + id);
    }


    public List<Estudiante> obtenerTodos(){

        List<Estudiante> estudiantesActivos = new ArrayList<>();

        for(Estudiante e: estudiantes){
            if(e.isActivo() == true){
                estudiantesActivos.add(e);
            }

        }

        return estudiantesActivos;
    }




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

    private void validarDatos(String nombre, String apellido, String email) {
        if (nombre == null || nombre.trim().isEmpty() || 
            apellido == null || apellido.trim().isEmpty() || 
            email == null || email.trim().isEmpty()) {
            throw new Model.Exceptions.datosInvalidosException("Nombre, apellido y correo electrónico son obligatorios.");
        }
    }

    public void eliminarEstudiante(String id){

        Estudiante e = buscarPorId(id);

        e.setActivo(false);

        notifyObservador(eventoModelo.ESTUDIANTE_ELIMINADO, e);
    }




}
