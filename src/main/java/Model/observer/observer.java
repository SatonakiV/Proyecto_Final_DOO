package Model.observer;

import Model.enums.eventoModelo;

/**
 * Contrato para quien emite avisos de cambios (el "observado") dentro del patrón Observer.
 * La implementan los servicios del Modelo que notifican cambios a sus observadores.
 */
public interface observer {
    /**
     * @param o observador a registrar
     */
    void agregarObservador(modelObserver o);

    /**
     * @param o observador a quitar del registro
     */
    void eliminarObservador(modelObserver o);

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    void notifyObservador(eventoModelo evento, Object datos);

}
