package Model.Strategy;

import Model.entidades.Tutor;

import java.util.List;

/**
 * Contrato común para las estrategias de búsqueda de tutores según distintos criterios.
 */
public interface strategyBusqueda {
    /**
     * @param tutores lista de tutores sobre la que se aplica el filtro
     * @param criterios criterios de búsqueda a evaluar
     * @return la lista de tutores que cumplen el criterio de esta estrategia
     */
    List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios);
}
