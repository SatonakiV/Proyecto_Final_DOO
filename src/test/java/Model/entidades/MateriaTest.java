package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MateriaTest {

    @Test
    void constructorAsignaDatosValidos() {
        Materia m = new Materia("Calculo", 20.0, 5);

        assertEquals("Calculo", m.getNombre());
        assertEquals(20.0, m.getTarifaPorHora());
        assertEquals(5, m.getCupoMaximoEstudiantes());
    }

    @Test
    void constructorLanzaExcepcionSiTarifaEsCero() {
        assertThrows(datosInvalidosException.class, () -> new Materia("Calculo", 0, 5));
    }

    @Test
    void constructorLanzaExcepcionSiTarifaEsNegativa() {
        assertThrows(datosInvalidosException.class, () -> new Materia("Calculo", -10, 5));
    }

    @Test
    void constructorLanzaExcepcionSiCupoEsCero() {
        assertThrows(datosInvalidosException.class, () -> new Materia("Calculo", 20, 0));
    }

    @Test
    void constructorLanzaExcepcionSiCupoEsNegativo() {
        assertThrows(datosInvalidosException.class, () -> new Materia("Calculo", 20, -3));
    }

    @Test
    void setTarifaPorHoraLanzaExcepcionConValorInvalido() {
        Materia m = new Materia("Calculo", 20, 5);
        assertThrows(datosInvalidosException.class, () -> m.setTarifaPorHora(0));
    }

    @Test
    void setTarifaPorHoraActualizaConValorValido() {
        Materia m = new Materia("Calculo", 20, 5);
        m.setTarifaPorHora(35.5);
        assertEquals(35.5, m.getTarifaPorHora());
    }

    @Test
    void setCupoMaximoEstudiantesLanzaExcepcionConValorInvalido() {
        Materia m = new Materia("Calculo", 20, 5);
        assertThrows(datosInvalidosException.class, () -> m.setCupoMaximoEstudiantes(0));
    }

    @Test
    void setCupoMaximoEstudiantesActualizaConValorValido() {
        Materia m = new Materia("Calculo", 20, 5);
        m.setCupoMaximoEstudiantes(10);
        assertEquals(10, m.getCupoMaximoEstudiantes());
    }
}
