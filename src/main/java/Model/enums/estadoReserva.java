package Model.enums;

/**
 * Representa los posibles estados de una reserva a lo largo de su ciclo de vida.
 */
public enum estadoReserva {

    ACTIVA("Activa"),
    CANCELADA("Cancelada"),
    COMPLETADA("Completada");

    private final String descripcion;

    estadoReserva(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return la descripción legible del estado
     */
    public String getDescripcion() {
        return descripcion;
    }
}
