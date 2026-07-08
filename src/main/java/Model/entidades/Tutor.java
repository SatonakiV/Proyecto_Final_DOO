package Model.entidades;

import Model.enums.diaSemana;
import Model.Exceptions.datosInvalidosException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa a un tutor del sistema, con sus datos de contacto, las materias que dicta
 * y los bloques de horario en los que está disponible. Valida internamente que no se
 * agreguen materias repetidas ni bloques de horario que se solapen entre sí.
 */
public class Tutor {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private List<Materia> materias;
    private List<BloqueHorario> bloquesDisponibilidad;
    private boolean activo;

    /**
     * Crea un tutor nuevo, activo por defecto, sin materias ni bloques de horario,
     * con un id generado automáticamente.
     *
     * @param nombre nombre del tutor
     * @param apellido apellido del tutor
     * @param email email de contacto del tutor
     * @param telefono teléfono de contacto del tutor
     */
    public Tutor(String nombre, String apellido, String email, String telefono) {
        this.id = UUID.randomUUID().toString(); // Genera el UUID automáticamente
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.materias = new ArrayList<>();
        this.bloquesDisponibilidad = new ArrayList<>();
        this.activo = true;
    }

    /**
     * Agrega una materia a la lista de materias que dicta el tutor.
     *
     * @param nuevaMateria materia a agregar
     * @throws datosInvalidosException si el tutor ya dicta una materia con el mismo nombre
     */
    public void agregarMateria(Materia nuevaMateria) {
        if (imparteMateria(nuevaMateria.getNombre())) {
            throw new datosInvalidosException("El tutor ya imparte la materia: " + nuevaMateria.getNombre());
        }
        this.materias.add(nuevaMateria);
    }

    /**
     * @param materia materia a quitar de la lista de materias del tutor
     */
    public void eliminarMateria(Materia materia) {
        this.materias.remove(materia);
    }

    /**
     * Agrega un bloque de disponibilidad horaria al tutor.
     *
     * @param nuevoBloque bloque de horario a agregar
     * @throws datosInvalidosException si el nuevo bloque se solapa con uno ya existente
     */
    public void agregarBloqueDisponibilidad(BloqueHorario nuevoBloque) {
        for (BloqueHorario bloqueExistente : bloquesDisponibilidad) {
            if (bloqueExistente.seSolapaCon(nuevoBloque)) {
                throw new datosInvalidosException("El nuevo bloque se solapa con uno existente.");
            }
        }
        this.bloquesDisponibilidad.add(nuevoBloque);
    }

    /**
     * @param bloque bloque de horario a quitar de la disponibilidad del tutor
     */
    public void eliminarBloqueDisponibilidad(BloqueHorario bloque) {
        this.bloquesDisponibilidad.remove(bloque);
    }

    /**
     * Revisa si el tutor tiene un bloque de disponibilidad que cubra por completo el
     * día y rango horario pedidos.
     *
     * @param dia día de la semana a revisar
     * @param inicio hora de inicio del rango pedido
     * @param fin hora de fin del rango pedido
     * @return true si el tutor tiene disponibilidad que cubre ese rango, false en caso contrario
     */
    public boolean tieneDisponibilidadEn(diaSemana dia, LocalTime inicio, LocalTime fin) {
        for (BloqueHorario bloque : bloquesDisponibilidad) {
            // Revisa si el bloque es del mismo día y cubre exactamente o es más amplio que el rango pedido
            if (bloque.getDia() == dia &&
                    !bloque.getHoraInicio().isAfter(inicio) &&
                    !bloque.getHoraFin().isBefore(fin)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param nombreMateria nombre de la materia a buscar
     * @return true si el tutor dicta una materia con ese nombre, false en caso contrario
     */
    public boolean imparteMateria(String nombreMateria) {
        for (Materia m : materias) {
            if (m.getNombre().equalsIgnoreCase(nombreMateria)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param nombreMateria nombre de la materia cuya tarifa se quiere obtener
     * @return la tarifa por hora que cobra el tutor por esa materia
     * @throws datosInvalidosException si el tutor no dicta esa materia
     */
    public double obtenerTarifaPorMateria(String nombreMateria) {
        for (Materia m : materias) {
            if (m.getNombre().equalsIgnoreCase(nombreMateria)) {
                return m.getTarifaPorHora();
            }
        }
        throw new datosInvalidosException("El tutor no imparte la materia: " + nombreMateria);
    }

    /**
     * @return el nombre y apellido del tutor concatenados en un solo texto
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * @return el id único del tutor
     */
    public String getId() { return id; }

    /**
     * @return el nombre del tutor
     */
    public String getNombre() { return nombre; }
    /**
     * @param nombre nuevo nombre del tutor
     */
    public void setNombre(String nombre) { this.nombre = nombre; } // Agregado

    /**
     * @return el apellido del tutor
     */
    public String getApellido() { return apellido; }
    /**
     * @param apellido nuevo apellido del tutor
     */
    public void setApellido(String apellido) { this.apellido = apellido; } // Agregado

    /**
     * @return el email del tutor
     */
    public String getEmail() { return email; }
    /**
     * @param email nuevo email del tutor
     */
    public void setEmail(String email) { this.email = email; } // Agregado

    /**
     * @return el teléfono del tutor
     */
    public String getTelefono() { return telefono; }
    /**
     * @param telefono nuevo teléfono del tutor
     */
    public void setTelefono(String telefono) { this.telefono = telefono; } // Agregado

    /**
     * @return la lista de materias que dicta el tutor
     */
    public List<Materia> getMaterias() { return materias; }
    /**
     * @return la lista de bloques de horario en los que el tutor está disponible
     */
    public List<BloqueHorario> getBloquesDisponibilidad() { return bloquesDisponibilidad; }

    /**
     * @return true si el tutor está activo, false si fue eliminado lógicamente
     */
    public boolean isActivo() { return activo; }
    /**
     * @param activo nuevo estado de actividad del tutor
     */
    public void setActivo(boolean activo) { this.activo = activo; }

    /**
     * @return una representación en texto del tutor con su nombre completo y cantidad de materias
     */
    @Override
    public String toString() {
        return getNombreCompleto() + " (Materias: " + materias.size() + ")";
    }
}