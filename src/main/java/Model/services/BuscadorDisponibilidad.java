package Model.services;

import Model.Strategy.criterioDeBusqueda;
import Model.Strategy.strategyBusqueda;
import Model.entidades.Tutor;
import Model.enums.eventoModelo;

import java.util.ArrayList;
import java.util.List;

public class BuscadorDisponibilidad {

    private TutorService tutorService;
    private List<strategyBusqueda> strategy;

    public BuscadorDisponibilidad(TutorService tutorService) {
        this.tutorService = tutorService;
        this.strategy = new ArrayList<>();

    }

    public void agregarStrategy(strategyBusqueda strategy) {
        this.strategy.add(strategy);
    }

    public void limpiarStrategy() {
        this.strategy.clear();
    }

    public void setStrategy(List<strategyBusqueda> strategy) {
        this.strategy = strategy;
    }

    public List<Tutor> buscarTutores(criterioDeBusqueda criterios) {
        List<Tutor> resultado = tutorService.obtenerTodos();

        for(strategyBusqueda strategy : strategy) {
            resultado = strategy.filtrar(resultado, criterios);
        }
        return resultado;
    }






}
