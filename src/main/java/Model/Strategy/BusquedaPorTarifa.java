package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorTarifa implements strategyBusqueda {

    public BusquedaPorTarifa() {}

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