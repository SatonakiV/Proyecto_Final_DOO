package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import Model.enums.diaSemana;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BloqueHorarioTest {

    @Test
    void constructorAsignaDatosValidos() {
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertEquals(diaSemana.LUNES, b.getDia());
        assertEquals(LocalTime.of(8, 0), b.getHoraInicio());
        assertEquals(LocalTime.of(10, 0), b.getHoraFin());
    }

    @Test
    void constructorLanzaExcepcionSiFinNoEsPosteriorAInicio() {
        assertThrows(datosInvalidosException.class,
                () -> new BloqueHorario(diaSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(8, 0)));
    }

    @Test
    void constructorLanzaExcepcionSiFinEsIgualAInicio() {
        assertThrows(datosInvalidosException.class,
                () -> new BloqueHorario(diaSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(10, 0)));
    }

    @Test
    void seSolapaConEsFalsoSiSonDiasDistintos() {
        BloqueHorario a = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        BloqueHorario b = new BloqueHorario(diaSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertFalse(a.seSolapaCon(b));
    }

    @Test
    void seSolapaConEsVerdaderoSiLosRangosSeCruzan() {
        BloqueHorario a = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));

        assertTrue(a.seSolapaCon(b));
        assertTrue(b.seSolapaCon(a));
    }

    @Test
    void seSolapaConEsFalsoSiLosRangosSonContiguos() {
        BloqueHorario a = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(12, 0));

        assertFalse(a.seSolapaCon(b));
        assertFalse(b.seSolapaCon(a));
    }

    @Test
    void seSolapaConEsVerdaderoSiUnBloqueContieneAlOtro() {
        BloqueHorario a = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0));
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertTrue(a.seSolapaCon(b));
    }

    @Test
    void contieneEvaluaLimitesCorrectamente() {
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertTrue(b.contiene(LocalTime.of(8, 0)));
        assertTrue(b.contiene(LocalTime.of(9, 59)));
        assertFalse(b.contiene(LocalTime.of(10, 0)));
        assertFalse(b.contiene(LocalTime.of(7, 59)));
    }

    @Test
    void getDuracionMinutosCalculaCorrectamente() {
        BloqueHorario b = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 30));
        assertEquals(150, b.getDuracionMinutos());
    }
}
