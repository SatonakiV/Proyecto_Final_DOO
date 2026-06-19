package Model.Strategy;

import Model.entidades.Tutor;
import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorMateria implements strategyBusqueda {

    public BusquedaPorMateria() {}

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