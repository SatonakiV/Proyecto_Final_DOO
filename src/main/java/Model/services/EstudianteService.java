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


    EstudianteService(){
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

        Estudiante nuevoEstudiante = new Estudiante(nombre, apellido, email, telefono);

        this.estudiantes.add(nuevoEstudiante);

        notifyObservador(eventoModelo.ESTUDIANTE_AGREGADO, nuevoEstudiante);
    }

    public void modificarEstudiante(String id, String nombre, String apellido, String email, String telefono){

        Estudiante e = buscarPorId(id);
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setEmail(email);
        e.setTelefono(telefono);

        notifyObservador(eventoModelo.ESTUDIANTE_MODIFICADO, e);

    }

    public void eliminarEstudiante(String id){

        Estudiante e = buscarPorId(id);

        e.setActivo(false);

        notifyObservador(eventoModelo.ESTUDIANTE_ELIMINADO, e);
    }




}
