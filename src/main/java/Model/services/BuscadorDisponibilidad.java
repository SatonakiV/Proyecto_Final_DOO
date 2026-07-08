package Model.services;

import Model.Strategy.criterioDeBusqueda;
import Model.Strategy.strategyBusqueda;
import Model.entidades.Tutor;
import Model.enums.eventoModelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Conecta el patrón Strategy con los datos reales de tutores: guarda una lista de
 * estrategias de búsqueda y las aplica una tras otra sobre todos los tutores hasta
 * quedarse con el resultado final filtrado.
 */
public class BuscadorDisponibilidad {

    private TutorService tutorService;
    private List<strategyBusqueda> strategy;

    /**
     * @param tutorService servicio del que se obtiene la lista completa de tutores a filtrar
     */
    public BuscadorDisponibilidad(TutorService tutorService) {
        this.tutorService = tutorService;
        this.strategy = new ArrayList<>();

    }

    /**
     * @param strategy estrategia de búsqueda a agregar a la cadena de filtrado
     */
    public void agregarStrategy(strategyBusqueda strategy) {
        this.strategy.add(strategy);
    }

    /**
     * Quita todas las estrategias de búsqueda agregadas hasta el momento.
     */
    public void limpiarStrategy() {
        this.strategy.clear();
    }

    /**
     * @param strategy nueva lista de estrategias de búsqueda a usar
     */
    public void setStrategy(List<strategyBusqueda> strategy) {
        this.strategy = strategy;
    }

    /**
     * @param criterios criterios de búsqueda a aplicar
     * @return la lista de tutores que cumplen con todas las estrategias agregadas
     */
    public List<Tutor> buscarTutores(criterioDeBusqueda criterios) {
        List<Tutor> resultado = tutorService.obtenerTodos();

        for(strategyBusqueda strategy : strategy) {
            resultado = strategy.filtrar(resultado, criterios);
        }
        return resultado;
    }






}
