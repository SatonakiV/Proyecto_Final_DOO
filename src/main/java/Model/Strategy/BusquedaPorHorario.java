package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorHorario implements strategyBusqueda {

    public BusquedaPorHorario() {}

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