package Model.services;

import Model.Exceptions.datosInvalidosException;
import Model.Exceptions.individuoNoEncontradoException;
import Model.entidades.Estudiante;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteServiceTest {

    private EstudianteService service;

    @BeforeEach
    void setUp() {
        service = new EstudianteService();
    }

    @Test
    void agregarEstudianteLoAgregaALaListaDeActivos() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");

        List<Estudiante> todos = service.obtenerTodos();
        assertEquals(1, todos.size());
        assertEquals("Ana", todos.get(0).getNombre());
    }

    @Test
    void agregarEstudianteConNombreVacioLanzaExcepcion() {
        assertThrows(datosInvalidosException.class,
                () -> service.agregarEstudiante("  ", "Perez", "ana@mail.com", "123"));
    }

    @Test
    void agregarEstudianteConEmailVacioLanzaExcepcion() {
        assertThrows(datosInvalidosException.class,
                () -> service.agregarEstudiante("Ana", "Perez", "", "123"));
    }

    @Test
    void agregarEstudianteConEmailDuplicadoLanzaExcepcion() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");

        assertThrows(datosInvalidosException.class,
                () -> service.agregarEstudiante("Otra", "Persona", "ANA@MAIL.COM", "999"));
    }

    @Test
    void buscarPorIdRetornaElEstudianteCorrecto() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");
        String id = service.obtenerTodos().get(0).getId();

        Estudiante encontrado = service.buscarPorId(id);

        assertEquals("Ana", encontrado.getNombre());
    }

    @Test
    void buscarPorIdInexistenteLanzaExcepcion() {
        assertThrows(individuoNoEncontradoException.class, () -> service.buscarPorId("no-existe"));
    }

    @Test
    void modificarEstudianteActualizaLosDatos() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");
        String id = service.obtenerTodos().get(0).getId();

        service.modificarEstudiante(id, "Ana Maria", "Perez", "anamaria@mail.com", "999");

        Estudiante actualizado = service.buscarPorId(id);
        assertEquals("Ana Maria", actualizado.getNombre());
        assertEquals("anamaria@mail.com", actualizado.getEmail());
    }

    @Test
    void modificarEstudianteConEmailDeOtroEstudianteLanzaExcepcion() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");
        service.agregarEstudiante("Luis", "Diaz", "luis@mail.com", "456");
        String idLuis = service.obtenerTodos().get(1).getId();

        assertThrows(datosInvalidosException.class,
                () -> service.modificarEstudiante(idLuis, "Luis", "Diaz", "ana@mail.com", "456"));
    }

    @Test
    void eliminarEstudianteEsUnBorradoLogico() {
        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");
        String id = service.obtenerTodos().get(0).getId();

        service.eliminarEstudiante(id);

        assertTrue(service.obtenerTodos().isEmpty());
        assertFalse(service.buscarPorId(id).isActivo());
    }

    @Test
    void agregarEstudianteNotificaAlObservador() {
        List<eventoModelo> eventosRecibidos = new ArrayList<>();
        modelObserver observer = (evento, datos) -> eventosRecibidos.add(evento);
        service.agregarObservador(observer);

        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");

        assertEquals(List.of(eventoModelo.ESTUDIANTE_AGREGADO), eventosRecibidos);
    }

    @Test
    void eliminarObservadorDejaDeNotificar() {
        List<eventoModelo> eventosRecibidos = new ArrayList<>();
        modelObserver observer = (evento, datos) -> eventosRecibidos.add(evento);
        service.agregarObservador(observer);
        service.eliminarObservador(observer);

        service.agregarEstudiante("Ana", "Perez", "ana@mail.com", "123");

        assertTrue(eventosRecibidos.isEmpty());
    }
}
