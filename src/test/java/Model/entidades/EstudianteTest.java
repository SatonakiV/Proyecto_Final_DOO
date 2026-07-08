package Model.entidades;

import Model.Exceptions.datosInvalidosException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteTest {

    @Test
    void constructorAsignaDatosYActivoPorDefecto() {
        Estudiante e = new Estudiante("Ana", "Perez", "ana@mail.com", "12345");

        assertEquals("Ana", e.getNombre());
        assertEquals("Perez", e.getApellido());
        assertEquals("ana@mail.com", e.getEmail());
        assertEquals("12345", e.getTelefono());
        assertTrue(e.isActivo());
        assertNotNull(e.getId());
    }

    @Test
    void dosEstudiantesTienenIdsDiferentes() {
        Estudiante a = new Estudiante("Ana", "Perez", "ana@mail.com", "1");
        Estudiante b = new Estudiante("Ana", "Perez", "ana@mail.com", "1");

        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void getNombreCompletoConcatenaNombreYApellido() {
        Estudiante e = new Estudiante("Ana", "Perez", "ana@mail.com", "1");
        assertEquals("Ana Perez", e.getNombreCompleto());
    }

    @Test

    void constructorLanzaExcepcionSiNombreEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Estudiante(null, "Perez", "ana@mail.com", "1"));
    }

    @Test
    void constructorLanzaExcepcionSiApellidoEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Estudiante("Ana", null, "ana@mail.com", "1"));
    }

    @Test
    void constructorLanzaExcepcionSiEmailEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Estudiante("Ana", "Perez", null, "1"));
    }

    @Test
    void constructorLanzaExcepcionSiTelefonoEsNull() {
        assertThrows(datosInvalidosException.class,
                () -> new Estudiante("Ana", "Perez", "ana@mail.com", null));
    }

    @Test
    void settersLanzanExcepcionConValorNull() {
        Estudiante e = new Estudiante("Ana", "Perez", "ana@mail.com", "1");

        assertThrows(datosInvalidosException.class, () -> e.setNombre(null));
        assertThrows(datosInvalidosException.class, () -> e.setApellido(null));
        assertThrows(datosInvalidosException.class, () -> e.setEmail(null));
        assertThrows(datosInvalidosException.class, () -> e.setTelefono(null));
    }

    @Test
    void setActivoCambiaElEstado() {
        Estudiante e = new Estudiante("Ana", "Perez", "ana@mail.com", "1");
        e.setActivo(false);
        assertFalse(e.isActivo());
    }
}
