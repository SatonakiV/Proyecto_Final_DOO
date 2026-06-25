package Model.entidades;

import Model.enums.diaSemana;
import Model.Exceptions.datosInvalidosException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tutor {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private List<Materia> materias;
    private List<BloqueHorario> bloquesDisponibilidad;
    private boolean activo;

    // Constructor completo
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

    // Agrega materia validando duplicados
    public void agregarMateria(Materia nuevaMateria) {
        if (imparteMateria(nuevaMateria.getNombre())) {
            throw new datosInvalidosException("El tutor ya imparte la materia: " + nuevaMateria.getNombre());
        }
        this.materias.add(nuevaMateria);
    }

    // Remueve una materia
    public void eliminarMateria(Materia materia) {
        this.materias.remove(materia);
    }

    // Agrega bloque validando solapamientos
    public void agregarBloqueDisponibilidad(BloqueHorario nuevoBloque) {
        for (BloqueHorario bloqueExistente : bloquesDisponibilidad) {
            if (bloqueExistente.seSolapaCon(nuevoBloque)) {
                throw new datosInvalidosException("El nuevo bloque se solapa con uno existente.");
            }
        }
        this.bloquesDisponibilidad.add(nuevoBloque);
    }

    // Remueve un bloque
    public void eliminarBloqueDisponibilidad(BloqueHorario bloque) {
        this.bloquesDisponibilidad.remove(bloque);
    }

    // Verifica si tiene disponibilidad en un rango específico
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

    // Verifica si imparte la materia
    public boolean imparteMateria(String nombreMateria) {
        for (Materia m : materias) {
            if (m.getNombre().equalsIgnoreCase(nombreMateria)) {
                return true;
            }
        }
        return false;
    }

    // Retorna la tarifa de la materia
    public double obtenerTarifaPorMateria(String nombreMateria) {
        for (Materia m : materias) {
            if (m.getNombre().equalsIgnoreCase(nombreMateria)) {
                return m.getTarifaPorHora();
            }
        }
        throw new datosInvalidosException("El tutor no imparte la materia: " + nombreMateria);
    }

    // Retorna nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // Getters y Setters
    public String getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; } // Agregado

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; } // Agregado

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; } // Agregado

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; } // Agregado

    public List<Materia> getMaterias() { return materias; }
    public List<BloqueHorario> getBloquesDisponibilidad() { return bloquesDisponibilidad; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return getNombreCompleto() + " (Materias: " + materias.size() + ")";
    }
}