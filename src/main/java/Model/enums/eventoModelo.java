package Model.enums;

/**
 * Representa los tipos de aviso que un servicio del Modelo puede notificar a sus
 * observadores cuando cambian los datos de tutores, estudiantes o reservas.
 */
public enum eventoModelo {

    TUTOR_AGREGADO,
    TUTOR_MODIFICADO,
    TUTOR_ELIMINADO,
    ESTUDIANTE_AGREGADO,
    ESTUDIANTE_MODIFICADO,
    ESTUDIANTE_ELIMINADO,
    RESERVA_CREADA,
    RESERVA_MODIFICADO,
    RESERVA_CANCELADA;
}
