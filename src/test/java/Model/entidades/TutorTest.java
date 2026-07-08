package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import Model.enums.diaSemana;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TutorTest {

    private Tutor crearTutor() {
        return new Tutor("Juan", "Gomez", "juan@mail.com", "12345");
    }

    @Test
    void constructorAsignaDatosYListasVacias() {
        Tutor t = crearTutor();

        assertEquals("Juan Gomez", t.getNombreCompleto());
        assertTrue(t.isActivo());
        assertNotNull(t.getId());
        assertTrue(t.getMaterias().isEmpty());
        assertTrue(t.getBloquesDisponibilidad().isEmpty());
    }

    @Test
    void agregarMateriaLaAgregaCorrectamente() {
        Tutor t = crearTutor();
        Materia m = new Materia("Calculo", 20, 5);

        t.agregarMateria(m);

        assertEquals(1, t.getMaterias().size());
        assertTrue(t.imparteMateria("Calculo"));
    }

    @Test
    void imparteMateriaEsInsensibleAMayusculas() {
        Tutor t = crearTutor();
        t.agregarMateria(new Materia("Calculo", 20, 5));

        assertTrue(t.imparteMateria("CALCULO"));
        assertTrue(t.imparteMateria("calculo"));
    }

    @Test
    void agregarMateriaDuplicadaLanzaExcepcion() {
        Tutor t = crearTutor();
        t.agregarMateria(new Materia("Calculo", 20, 5));

        assertThrows(datosInvalidosException.class,
                () -> t.agregarMateria(new Materia("Calculo", 30, 3)));
    }

    @Test
    void agregarMateriaDuplicadaEsInsensibleAMayusculas() {
        Tutor t = crearTutor();
        t.agregarMateria(new Materia("Calculo", 20, 5));

        assertThrows(datosInvalidosException.class,
                () -> t.agregarMateria(new Materia("CALCULO", 30, 3)));
    }

    @Test
    void eliminarMateriaLaQuitaDeLaLista() {
        Tutor t = crearTutor();
        Materia m = new Materia("Calculo", 20, 5);
        t.agregarMateria(m);

        t.eliminarMateria(m);

        assertTrue(t.getMaterias().isEmpty());
    }

    @Test
    void obtenerTarifaPorMateriaRetornaLaTarifaCorrecta() {
        Tutor t = crearTutor();
        t.agregarMateria(new Materia("Calculo", 25.5, 5));

        assertEquals(25.5, t.obtenerTarifaPorMateria("Calculo"));
    }

    @Test
    void obtenerTarifaPorMateriaLanzaExcepcionSiNoLaImparte() {
        Tutor t = crearTutor();

        assertThrows(datosInvalidosException.class, () -> t.obtenerTarifaPorMateria("Fisica"));
    }

    @Test
    void agregarBloqueDisponibilidadLoAgregaCorrectamente() {
        Tutor t = crearTutor();
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));

        t.agregarBloqueDisponibilidad(b);

        assertEquals(1, t.getBloquesDisponibilidad().size());
    }

    @Test
    void agregarBloqueQueSeSolapaLanzaExcepcion() {
        Tutor t = crearTutor();
        t.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));

        assertThrows(datosInvalidosException.class,
                () -> t.agregarBloqueDisponibilidad(
                        new BloqueHorario(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0))));
    }

    @Test
    void agregarBloqueEnDiaDistintoNoLanzaExcepcion() {
        Tutor t = crearTutor();
        t.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));

        assertDoesNotThrow(() -> t.agregarBloqueDisponibilidad(
                new BloqueHorario(diaSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(10, 0))));
    }

    @Test
    void eliminarBloqueDisponibilidadLoQuitaDeLaLista() {
        Tutor t = crearTutor();
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        t.agregarBloqueDisponibilidad(b);

        t.eliminarBloqueDisponibilidad(b);

        assertTrue(t.getBloquesDisponibilidad().isEmpty());
    }

    @Test
    void tieneDisponibilidadEnEsVerdaderoSiElRangoEstaCubierto() {
        Tutor t = crearTutor();
        t.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));

        assertTrue(t.tieneDisponibilidadEn(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0)));
    }

    @Test
    void tieneDisponibilidadEnEsFalsoSiElRangoExcedeElBloque() {
        Tutor t = crearTutor();
        t.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0)));

        assertFalse(t.tieneDisponibilidadEn(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));
    }

    @Test
    void tieneDisponibilidadEnEsFalsoSiElDiaNoCoincide() {
        Tutor t = crearTutor();
        t.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));

        assertFalse(t.tieneDisponibilidadEn(diaSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(10, 0)));
    }

    @Test
    void setActivoCambiaElEstado() {
        Tutor t = crearTutor();
        t.setActivo(false);
        assertFalse(t.isActivo());
    }
}
