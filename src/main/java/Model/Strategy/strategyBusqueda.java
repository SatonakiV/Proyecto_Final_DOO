package Model.Strategy;

import java.util.List;

public interface strategyBusqueda {
    List<tutor> filtrar(List<tutor> tutores, criterioDeBusqueda criterios);
}
