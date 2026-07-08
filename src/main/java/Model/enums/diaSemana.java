package Model.enums;

/**
 * Representa los días de la semana en los que puede ubicarse un bloque de horario.
 */
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

    /**
     * @return la etiqueta legible del día de la semana
     */
    public String getEtiqueta() {
        return etiqueta;
    }
}
