package Model.services;

import Model.Exceptions.datosInvalidosException;
import Model.Exceptions.individuoNoEncontradoException;
import Model.entidades.BloqueHorario;
import Model.entidades.Materia;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TutorServiceTest {

    private TutorService service;

    @BeforeEach
    void setUp() {
        service = new TutorService();
    }

    @Test
    void agregarTutorLoAgregaALaListaDeActivos() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "123");

        List<Tutor> todos = service.obtenerTodos();
        assertEquals(1, todos.size());
        assertEquals("Juan", todos.get(0).getNombre());
    }

    @Test
    void agregarTutorConNombreVacioLanzaExcepcion() {
        assertThrows(datosInvalidosException.class,
                () -> service.agregarTutor("  ", "Gomez", "juan@mail.com", "123"));
    }

    @Test
    void agregarTutorConApellidoVacioLanzaExcepcion() {
        assertThrows(datosInvalidosException.class,
                () -> service.agregarTutor("Juan", "", "juan@mail.com", "123"));
    }

    @Test
    void buscarPorIdInexistenteLanzaExcepcion() {
        assertThrows(individuoNoEncontradoException.class, () -> service.buscarPorId("no-existe"));
    }

    @Test
    void modificarTutorActualizaLosDatos() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "123");
        String id = service.obtenerTodos().get(0).getId();

        service.modificarTutor(id, "Juan Carlos", "Gomez", "jc@mail.com", "999");

        Tutor actualizado = service.buscarPorId(id);
        assertEquals("Juan Carlos", actualizado.getNombre());
        assertEquals("jc@mail.com", actualizado.getEmail());
    }

    @Test
    void eliminarTutorEsUnBorradoLogico() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "123");
        String id = service.obtenerTodos().get(0).getId();

        service.eliminarTutor(id);

        assertTrue(service.obtenerTodos().isEmpty());
        assertFalse(service.buscarPorId(id).isActivo());
    }

    @Test
    void obtenerMateriasUnicasNoRepiteNombres() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        service.agregarTutor("Ana", "Diaz", "ana@mail.com", "2");
        String idJuan = service.obtenerTodos().get(0).getId();
        String idAna = service.obtenerTodos().get(1).getId();

        service.agregarMateriaATutor(idJuan, new Materia("Calculo", 20, 5));
        service.agregarMateriaATutor(idAna, new Materia("Calculo", 25, 3));
        service.agregarMateriaATutor(idAna, new Materia("Fisica", 30, 4));

        List<String> materias = service.obtenerMateriasUnicas();
        assertEquals(2, materias.size());
        assertTrue(materias.contains("Calculo"));
        assertTrue(materias.contains("Fisica"));
    }

    @Test
    void agregarMateriaATutorDuplicadaLanzaExcepcion() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        String id = service.obtenerTodos().get(0).getId();
        service.agregarMateriaATutor(id, new Materia("Calculo", 20, 5));

        assertThrows(datosInvalidosException.class,
                () -> service.agregarMateriaATutor(id, new Materia("Calculo", 30, 3)));
    }

    @Test
    void agregarBloqueATutorLoAgregaCorrectamente() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        String id = service.obtenerTodos().get(0).getId();

        service.agregarBloqueATutor(id, new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));

        assertEquals(1, service.buscarPorId(id).getBloquesDisponibilidad().size());
    }

    @Test
    void agregarBloqueATutorQueSeSolapaLanzaExcepcion() {
        service.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        String id = service.obtenerTodos().get(0).getId();
        service.agregarBloqueATutor(id, new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));

        assertThrows(datosInvalidosException.class,
                () -> service.agregarBloqueATutor(id,
                        new BloqueHorario(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0))));
    }
}
