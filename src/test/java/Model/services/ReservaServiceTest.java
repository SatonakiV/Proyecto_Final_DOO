package Model.services;

import Model.Exceptions.conflictoHorarioException;
import Model.Exceptions.cupoMaximoException;
import Model.Exceptions.individuoNoEncontradoException;
import Model.Exceptions.tutorNoDisponibleException;
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

import static org.junit.jupiter.api.Assertions.*;

class ReservaServiceTest {

    private ReservaService service;
    private Tutor tutor;
    private Materia materia;
    private BloqueHorario disponibilidad;

    @BeforeEach
    void setUp() {
        service = new ReservaService();
        tutor = new Tutor("Juan", "Gomez", "juan@mail.com", "1");
        materia = new Materia("Calculo", 20, 2);
        disponibilidad = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0));
        tutor.agregarMateria(materia);
        tutor.agregarBloqueDisponibilidad(disponibilidad);
    }

    private Estudiante estudiante(String email) {
        return new Estudiante("Ana", "Perez", email, "2");
    }

    private BloqueHorario claseBloque(int horaIni, int horaFin) {
        return new BloqueHorario(diaSemana.LUNES, LocalTime.of(horaIni, 0), LocalTime.of(horaFin, 0));
    }

    @Test
    void crearReservaExitosaQuedaActivaYSeAgregaALaLista() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");

        List<Reserva> todas = service.obtenerTodas();
        assertEquals(1, todas.size());
        assertTrue(todas.get(0).estaActiva());
    }

    @Test
    void crearReservaFueraDeLaDisponibilidadDelTutorLanzaExcepcion() {
        BloqueHorario fueraDeHorario = new BloqueHorario(diaSemana.LUNES, LocalTime.of(14, 0), LocalTime.of(15, 0));

        assertThrows(tutorNoDisponibleException.class,
                () -> service.crearReserva(tutor, estudiante("ana@mail.com"), materia, fueraDeHorario, "n"));
    }

    @Test
    void crearReservaConChoqueDeHorarioDelEstudianteLanzaExcepcion() {
        Estudiante est = estudiante("ana@mail.com");
        service.crearReserva(tutor, est, materia, claseBloque(8, 9), "n");

        assertThrows(conflictoHorarioException.class,
                () -> service.crearReserva(tutor, est, materia, claseBloque(8, 10), "n"));
    }

    @Test
    void crearReservaSuperandoElCupoMaximoLanzaExcepcion() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");
        service.crearReserva(tutor, estudiante("luis@mail.com"), materia, claseBloque(8, 9), "n");

        assertThrows(cupoMaximoException.class,
                () -> service.crearReserva(tutor, estudiante("otro@mail.com"), materia, claseBloque(8, 9), "n"));
    }

    @Test
    void contarEstudianteEnBloqueCuentaSoloReservasActivasYQueSeSolapan() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");

        int contador = service.contarEstudianteEnBloque(tutor.getId(), materia.getNombre(), claseBloque(8, 9));

        assertEquals(1, contador);
    }

    @Test
    void cancelarReservaLaMarcaComoInactivaYLiberaCupo() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");
        Reserva r = service.obtenerTodas().get(0);

        service.cancelarReserva(r.getId());

        assertFalse(r.estaActiva());
        assertEquals(0, service.contarEstudianteEnBloque(tutor.getId(), materia.getNombre(), claseBloque(8, 9)));
    }

    @Test
    void buscarPorIdInexistenteLanzaExcepcion() {
        assertThrows(individuoNoEncontradoException.class, () -> service.buscarPorId("no-existe"));
    }

    @Test
    void obtenerReservasActivasPorTutorFiltraCorrectamente() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");
        Tutor otroTutor = new Tutor("Otro", "Tutor", "otro@mail.com", "9");

        List<Reserva> resultado = service.obtenerReservasActivasPorTutor(tutor.getId());
        List<Reserva> vacio = service.obtenerReservasActivasPorTutor(otroTutor.getId());

        assertEquals(1, resultado.size());
        assertTrue(vacio.isEmpty());
    }

    @Test
    void obtenerReservasActivasPorEstudianteFiltraCorrectamente() {
        Estudiante est = estudiante("ana@mail.com");
        service.crearReserva(tutor, est, materia, claseBloque(8, 9), "n");

        List<Reserva> resultado = service.obtenerReservasActivasPorEstudiante(est.getId());

        assertEquals(1, resultado.size());
    }

    @Test
    void modificarReservaCambiaElBloqueHorario() {
        service.crearReserva(tutor, estudiante("ana@mail.com"), materia, claseBloque(8, 9), "n");
        Reserva r = service.obtenerTodas().get(0);
        BloqueHorario nuevoBloque = claseBloque(10, 11);

        service.modificarReserva(r.getId(), nuevoBloque);

        assertEquals(nuevoBloque, r.getBloqueHorario());
    }
}
