package Model.enums;

public enum diaSemana {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String etiqueta;
    diaSemana(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    public String getEtiqueta() {
        return etiqueta;
    }
}
