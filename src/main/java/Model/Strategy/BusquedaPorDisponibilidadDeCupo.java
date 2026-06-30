package Model.Strategy;

import Model.entidades.Tutor;
import Model.entidades.Materia;
import Model.entidades.BloqueHorario;
import Model.services.ReservaService;
import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorDisponibilidadDeCupo implements strategyBusqueda {

    private ReservaService reservaService;

    public BusquedaPorDisponibilidadDeCupo(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

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