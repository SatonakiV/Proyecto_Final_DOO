package Model.Strategy;

import Model.entidades.Tutor;

import java.util.List;

public interface strategyBusqueda {
    List<Tutor> filtrar(List<Tutor> tutores, criterioDeBusqueda criterios);
}
