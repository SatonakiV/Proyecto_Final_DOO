package Model.observer;

import Model.enums.eventoModelo;

public interface observer {
    void agregarObservador(modelObserver o);
    void eliminarObservador(modelObserver o);
    void notifyObservador(eventoModelo evento, Object datos);

}
