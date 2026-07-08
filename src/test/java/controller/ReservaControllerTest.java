package controller;

import Model.entidades.BloqueHorario;
import Model.entidades.Estudiante;
import Model.entidades.Materia;
import Model.entidades.Reserva;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import Model.services.BuscadorDisponibilidad;
import Model.services.ReservaService;
import Model.services.TutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservaControllerTest {

    private ReservaService reservaService;
    private TutorService tutorService;
    private ReservaController controller;

    private Tutor tutor;
    private Materia materia;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
        tutorService = new TutorService();
        BuscadorDisponibilidad buscador = new BuscadorDisponibilidad(tutorService);
        controller = new ReservaController(reservaService, buscador);

        tutorService.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        tutor = tutorService.obtenerTodos().get(0);
        materia = new Materia("Calculo", 20, 1);
        tutorService.agregarMateriaATutor(tutor.getId(), materia);
        tutorService.agregarBloqueATutor(tutor.getId(),
                new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));

        estudiante = new Estudiante("Ana", "Perez", "ana@mail.com", "2");
    }

    private BloqueHorario bloque(int horaIni, int horaFin) {
        return new BloqueHorario(diaSemana.LUNES, LocalTime.of(horaIni, 0), LocalTime.of(horaFin, 0));
    }

    @Test
    void agendarClaseExitosaRetornaMensajeDeExito() {
        String mensaje = controller.agendarClase(estudiante, tutor, materia, bloque(8, 9), "n");

        assertEquals("Reserva guardada exitosamente.", mensaje);
        assertEquals(1, controller.obtenerTodas().size());
    }

    @Test
    void agendarClaseFueraDeHorarioRetornaMensajeDeProfesorNoDisponible() {
        String mensaje = controller.agendarClase(estudiante, tutor, materia, bloque(14, 15), "n");

        assertEquals("Profesor no disponible.", mensaje);
    }

    @Test
    void agendarClaseConChoqueDeHorarioRetornaMensajeDeError() {
        controller.agendarClase(estudiante, tutor, materia, bloque(8, 9), "n");

        String mensaje = controller.agendarClase(estudiante, tutor, materia, bloque(8, 10), "n");

        assertEquals("Error, horario ya tomado.", mensaje);
    }

    @Test
    void agendarClaseSuperandoCupoRetornaMensajeDeSinCupos() {
        controller.agendarClase(estudiante, tutor, materia, bloque(8, 9), "n");
        Estudiante otro = new Estudiante("Luis", "Diaz", "luis@mail.com", "3");

        String mensaje = controller.agendarClase(otro, tutor, materia, bloque(8, 9), "n");

        assertEquals("Sin cupos disponibles.", mensaje);
    }

    @Test
    void anularConIdValidoRetornaMensajeDeExitoYCancelaLaReserva() {
        controller.agendarClase(estudiante, tutor, materia, bloque(8, 9), "n");
        Reserva r = controller.obtenerTodas().get(0);

        String mensaje = controller.anular(r.getId());

        assertEquals("Reserva anulada con éxito.", mensaje);
        assertFalse(r.estaActiva());
    }

    @Test
    void anularConIdInexistenteRetornaMensajeDeError() {
        String mensaje = controller.anular("no-existe");

        assertEquals("Error al anular la reserva.", mensaje);
    }

    @Test
    void buscarTutoresDisponiblesRetornaSoloElTutorConCupo() {
        List<Tutor> resultado = controller.buscarTutoresDisponibles("Calculo", diaSemana.LUNES,
                LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertEquals(1, resultado.size());
        assertEquals(tutor.getId(), resultado.get(0).getId());
    }

    @Test
    void buscarTutoresDisponiblesNoIncluyeAlTutorSinCupo() {
        controller.agendarClase(estudiante, tutor, materia, bloque(8, 9), "n");

        List<Tutor> resultado = controller.buscarTutoresDisponibles("Calculo", diaSemana.LUNES,
                LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertTrue(resultado.isEmpty());
    }
}
