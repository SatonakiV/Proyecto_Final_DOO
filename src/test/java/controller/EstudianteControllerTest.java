package controller;

import Model.entidades.Estudiante;
import Model.services.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteControllerTest {

    private EstudianteService service;
    private EstudianteController controller;

    @BeforeEach
    void setUp() {
        service = new EstudianteService();
        controller = new EstudianteController(service);
    }

    @Test
    void registrarConDatosValidosRetornaMensajeDeExito() {
        String mensaje = controller.registrar("Ana", "Perez", "ana@mail.com", "123");

        assertEquals("Estudiante registrado con éxito.", mensaje);
        assertEquals(1, controller.obtenerTodos().size());
    }

    @Test
    void registrarConDatosInvalidosRetornaMensajeDeError() {
        String mensaje = controller.registrar("", "Perez", "ana@mail.com", "123");

        assertEquals("Error al registrar", mensaje);
        assertTrue(controller.obtenerTodos().isEmpty());
    }

    @Test
    void modificarConDatosValidosRetornaMensajeDeExito() {
        controller.registrar("Ana", "Perez", "ana@mail.com", "123");
        String id = controller.obtenerTodos().get(0).getId();

        String mensaje = controller.modificar(id, "Ana Maria", "Perez", "anamaria@mail.com", "999");

        assertEquals("Estudiante modificado con éxito.", mensaje);
        assertEquals("Ana Maria", controller.buscarPorId(id).getNombre());
    }

    @Test
    void modificarConIdInexistenteRetornaMensajeDeError() {
        String mensaje = controller.modificar("no-existe", "Ana", "Perez", "ana@mail.com", "123");

        assertTrue(mensaje.startsWith("Error al modificar"));
    }

    @Test
    void eliminarConIdValidoRetornaMensajeDeExitoYBorraLogicamente() {
        controller.registrar("Ana", "Perez", "ana@mail.com", "123");
        String id = controller.obtenerTodos().get(0).getId();

        String mensaje = controller.eliminar(id);

        assertEquals("Estudiante eliminado con éxito.", mensaje);
        assertTrue(controller.obtenerTodos().isEmpty());
        assertFalse(controller.buscarPorId(id).isActivo());
    }

    @Test
    void eliminarConIdInexistenteRetornaMensajeDeError() {
        String mensaje = controller.eliminar("no-existe");

        assertTrue(mensaje.startsWith("Error al eliminar"));
    }

    @Test
    void obtenerTodosDelegaEnElServicio() {
        controller.registrar("Ana", "Perez", "ana@mail.com", "123");

        List<Estudiante> todos = controller.obtenerTodos();

        assertEquals(1, todos.size());
    }
}
