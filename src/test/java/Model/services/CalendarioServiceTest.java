package Model.services;

import Model.entidades.BloqueHorario;
import Model.entidades.Estudiante;
import Model.entidades.Materia;
import Model.entidades.Reserva;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CalendarioServiceTest {

    private ReservaService reservaService;
    private CalendarioService calendarioService;
    private Tutor tutor;
    private Estudiante estudiante;
    private Materia materia;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
        calendarioService = new CalendarioService(reservaService);

        tutor = new Tutor("Juan", "Gomez", "juan@mail.com", "1");
        estudiante = new Estudiante("Ana", "Perez", "ana@mail.com", "2");
        materia = new Materia("Calculo", 20, 5);
        tutor.agregarMateria(materia);
        tutor.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));

        reservaService.crearReserva(tutor, estudiante, materia,
                new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0)), "n");
    }

    @Test
    void obtenerReservasSemanalPorTutorContieneTodosLosDiasDeLaSemana() {
        Map<diaSemana, List<Reserva>> mapa = calendarioService.obtenerReservasSemanalPorTutor(tutor.getId());

        assertEquals(diaSemana.values().length, mapa.size());
        for (diaSemana dia : diaSemana.values()) {
            assertNotNull(mapa.get(dia));
        }
    }

    @Test
    void obtenerReservasSemanalPorTutorUbicaLaReservaEnElDiaCorrecto() {
        Map<diaSemana, List<Reserva>> mapa = calendarioService.obtenerReservasSemanalPorTutor(tutor.getId());

        assertEquals(1, mapa.get(diaSemana.LUNES).size());
        assertTrue(mapa.get(diaSemana.MARTES).isEmpty());
    }

    @Test
    void obtenerReservasSemanalPorEstudianteUbicaLaReservaEnElDiaCorrecto() {
        Map<diaSemana, List<Reserva>> mapa = calendarioService.obtenerReservasSemanalPorEstudiante(estudiante.getId());

        assertEquals(1, mapa.get(diaSemana.LUNES).size());
    }

    @Test
    void obtenerTodasLasReservasSemanalExcluyeCanceladas() {
        Reserva r = reservaService.obtenerTodas().get(0);
        r.cancelar();

        Map<diaSemana, List<Reserva>> mapa = calendarioService.obtenerTodasLasReservasSemanal();

        assertTrue(mapa.get(diaSemana.LUNES).isEmpty());
    }

    @Test
    void obtenerTodasLasReservasExcluyeCanceladas() {
        Reserva r = reservaService.obtenerTodas().get(0);
        r.cancelar();

        List<Reserva> activas = calendarioService.obtenerTodasLasReservas();

        assertTrue(activas.isEmpty());
    }

    @Test
    void obtenerBloquesOcupadosFiltraPorDia() {
        List<BloqueHorario> ocupadosLunes = calendarioService.obtenerBloquesOcupados(tutor.getId(), diaSemana.LUNES);
        List<BloqueHorario> ocupadosMartes = calendarioService.obtenerBloquesOcupados(tutor.getId(), diaSemana.MARTES);

        assertEquals(1, ocupadosLunes.size());
        assertTrue(ocupadosMartes.isEmpty());
    }
}
