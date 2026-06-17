package Model.enums;

public enum estadoReserva {

    ACTIVA("Activa"),
    CANCELADA("Cancelada"),
    COMPLETADA("Completada");

    private final String descripcion;

    estadoReserva(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
