package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda que deja solo los tutores que dictan la materia pedida.
 */
public class BusquedaPorMateria implements strategyBusqueda {

    public BusquedaPorMateria() {}

    /**
     * @param tutores lista de tutores sobre la que se aplica el filtro
     * @param criterios criterios de búsqueda; solo se usa la materia
     * @return los tutores de la lista original que dictan la materia pedida, o la lista sin cambios si no se pidió materia
     */
    @Override
    public List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios) {
        if (criterios.getMateria() == null || criterios.getMateria().isEmpty()) {
            return tutores;
        }

        return tutores.stream()
                .filter(tutor -> tutor.imparteMateria(criterios.getMateria()))
                .collect(Collectors.toList());
    }
}