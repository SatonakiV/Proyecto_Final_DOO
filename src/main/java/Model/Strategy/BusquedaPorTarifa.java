package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda que deja solo los tutores cuya tarifa para la materia pedida
 * no supera la tarifa máxima indicada.
 */
public class BusquedaPorTarifa implements strategyBusqueda {

    public BusquedaPorTarifa() {}

    /**
     * @param tutores lista de tutores sobre la que se aplica el filtro
     * @param criterios criterios de búsqueda; solo se usan la materia y la tarifa máxima
     * @return los tutores de la lista original que dictan la materia con una tarifa dentro del máximo, o la lista sin cambios si no se pidió materia o tarifa máxima
     */
    @Override
    public List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios) {
        if (criterios.getMateria() == null || criterios.getTarifaMaxima() == null) {
            return tutores;
        }

        return tutores.stream()
                .filter(tutor -> tutor.imparteMateria(criterios.getMateria()) &&
                        tutor.obtenerTarifaPorMateria(criterios.getMateria()) <= criterios.getTarifaMaxima())
                .collect(Collectors.toList());
    }
}