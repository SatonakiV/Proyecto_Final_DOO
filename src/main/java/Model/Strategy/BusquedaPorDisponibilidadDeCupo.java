package Model.Strategy;

import Model.entidades.Tutor;
import Model.entidades.Materia;
import Model.entidades.BloqueHorario;
import Model.services.ReservaService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda más completa: deja solo los tutores que dictan la materia pedida
 * y que además todavía tienen cupo disponible (menos estudiantes inscritos que el cupo
 * máximo de la materia) en el día y horario pedido.
 */
public class BusquedaPorDisponibilidadDeCupo implements strategyBusqueda {

    private ReservaService reservaService;

    /**
     * @param reservaService servicio usado para contar cuántos estudiantes ya tiene inscritos un tutor en un bloque
     */
    public BusquedaPorDisponibilidadDeCupo(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * @param tutores lista de tutores sobre la que se aplica el filtro
     * @param criterios criterios de búsqueda; se usan la materia, el día, la hora de inicio y la hora de fin
     * @return los tutores de la lista original que dictan la materia y tienen cupo disponible en ese horario, o la lista sin cambios si falta algún criterio
     */
    @Override
    public List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios) {
        if (criterios.getMateria() == null || criterios.getDia() == null ||
                criterios.getHoraInicio() == null || criterios.getHoraFin() == null) {
            return tutores;
        }

        BloqueHorario bloque = new BloqueHorario(criterios.getDia(), criterios.getHoraInicio(), criterios.getHoraFin());

        return tutores.stream()
                .filter(tutor -> {
                    if (!tutor.imparteMateria(criterios.getMateria())) return false;

                    int estudiantesActuales = reservaService.contarEstudianteEnBloque(
                            tutor.getId(), criterios.getMateria(), bloque);

                    int cupoMaximo = tutor.getMaterias().stream()
                            .filter(m -> m.getNombre().equals(criterios.getMateria()))
                            .findFirst()
                            .map(Materia::getCupoMaximoEstudiantes)
                            .orElse(0);

                    return estudiantesActuales < cupoMaximo;
                })
                .collect(Collectors.toList());
    }
}