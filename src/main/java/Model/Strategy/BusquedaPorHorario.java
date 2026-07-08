package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda que deja solo los tutores disponibles en el día y horario pedido.
 */
public class BusquedaPorHorario implements strategyBusqueda {

    public BusquedaPorHorario() {}

    /**
     * @param tutores lista de tutores sobre la que se aplica el filtro
     * @param criterios criterios de búsqueda; solo se usan el día, la hora de inicio y la hora de fin
     * @return los tutores de la lista original disponibles en ese día y horario, o la lista sin cambios si no se pidió horario
     */
    @Override
    public List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios) {
        if (criterios.getDia() == null || criterios.getHoraInicio() == null || criterios.getHoraFin() == null) {
            return tutores;
        }

        return tutores.stream()
                .filter(tutor -> tutor.tieneDisponibilidadEn(criterios.getDia(), criterios.getHoraInicio(), criterios.getHoraFin()))
                .collect(Collectors.toList());
    }
}