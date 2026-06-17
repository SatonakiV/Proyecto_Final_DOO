package Model.observer;

import Model.enums.eventoModelo;

public interface modelObserver {
    void onModeloActualizado(eventoModelo evento, Object datos);
}
