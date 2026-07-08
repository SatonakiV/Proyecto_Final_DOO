package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import Model.enums.diaSemana;
import Model.enums.estadoReserva;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Tutor tutor() {
        return new Tutor("Juan", "Gomez", "juan@mail.com", "1");
    }

    private Estudiante estudiante() {
        return new Estudiante("Ana", "Perez", "ana@mail.com", "2");
    }

    private Materia materia() {
        return new Materia("Calculo", 20, 5);
    }

    private BloqueHorario bloque(diaSemana dia, int horaIni, int horaFin) {
        return new BloqueHorario(dia, LocalTime.of(horaIni, 0), LocalTime.of(horaFin, 0));
    }

    @Test
    void constructorAsignaDatosYEstadoActivo() {
        Reserva r = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "nota");

        assertEquals(estadoReserva.ACTIVA, r.getEstado());
        assertTrue(r.estaActiva());
        assertNotNull(r.getId());
        assertNotNull(r.getFechaCreacion());
        assertEquals("nota", r.getNotas());
    }

    @Test
    void constructorLanzaExcepcionSiTutorEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Reserva(null, estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n"));
    }

    @Test
    void constructorLanzaExcepcionSiEstudianteEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Reserva(tutor(), null, materia(), bloque(diaSemana.LUNES, 8, 10), "n"));
    }

    @Test
    void constructorLanzaExcepcionSiMateriaEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Reserva(tutor(), estudiante(), null, bloque(diaSemana.LUNES, 8, 10), "n"));
    }

    @Test
    void constructorLanzaExcepcionSiBloqueEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Reserva(tutor(), estudiante(), materia(), null, "n"));
    }

    @Test
    void cancelarCambiaElEstadoYEstaActivaEsFalso() {
        Reserva r = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n");

        r.cancelar();

        assertEquals(estadoReserva.CANCELADA, r.getEstado());
        assertFalse(r.estaActiva());
    }

    @Test
    void completarCambiaElEstado() {
        Reserva r = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n");

        r.completar();

        assertEquals(estadoReserva.COMPLETADA, r.getEstado());
        assertFalse(r.estaActiva());
    }

    @Test
    void conflictaConEsVerdaderoSiLosBloquesSeSolapan() {
        Reserva a = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n");
        Reserva b = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 9, 11), "n");

        assertTrue(a.conflictaCon(b));
    }

    @Test
    void conflictaConEsFalsoSiLosBloquesNoSeSolapan() {
        Reserva a = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n");
        Reserva b = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.MARTES, 8, 10), "n");

        assertFalse(a.conflictaCon(b));
    }

    @Test
    void setBloqueHorarioActualizaElBloque() {
        Reserva r = new Reserva(tutor(), estudiante(), materia(), bloque(diaSemana.LUNES, 8, 10), "n");
        BloqueHorario nuevo = bloque(diaSemana.MIERCOLES, 14, 16);

        r.setBloqueHorario(nuevo);

        assertEquals(nuevo, r.getBloqueHorario());
    }
}
