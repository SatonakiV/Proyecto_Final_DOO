package Model.observer;

import Model.enums.eventoModelo;

/**
 * Contrato para quien recibe avisos de cambios (el observador real) dentro del patrón
 * Observer. La implementan los paneles de la Vista que se refrescan cuando el Modelo
 * notifica un cambio.
 */
public interface modelObserver {
    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    void onModeloActualizado(eventoModelo evento, Object datos);
}
