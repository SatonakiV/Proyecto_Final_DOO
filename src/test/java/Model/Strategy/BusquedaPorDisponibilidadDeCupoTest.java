package Model.Strategy;

import Model.entidades.BloqueHorario;
import Model.entidades.Estudiante;
import Model.entidades.Materia;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import Model.services.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusquedaPorDisponibilidadDeCupoTest {

    private ReservaService reservaService;
    private BusquedaPorDisponibilidadDeCupo strategy;
    private Tutor tutor;
    private Materia materia;
    private BloqueHorario disponibilidad;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
        strategy = new BusquedaPorDisponibilidadDeCupo(reservaService);

        tutor = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        materia = new Materia("Calculo", 20, 1);
        disponibilidad = new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0));
        tutor.agregarMateria(materia);
        tutor.agregarBloqueDisponibilidad(disponibilidad);
    }

    private criterioDeBusqueda criterioParaBloque(int horaIni, int horaFin) {
        return new criterioDeBusqueda("Calculo", diaSemana.LUNES, LocalTime.of(horaIni, 0), LocalTime.of(horaFin, 0), null);
    }

    @Test
    void filtrarIncluyeTutorConCupoDisponible() {
        List<Tutor> resultado = strategy.filtrar(List.of(tutor), criterioParaBloque(8, 9));

        assertEquals(1, resultado.size());
    }

    @Test
    void filtrarExcluyeTutorSinCupoDisponible() {
        Estudiante estudiante = new Estudiante("Ana", "Perez", "ana@mail.com", "2");
        reservaService.crearReserva(tutor, estudiante, materia,
                new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0)), "n");

        List<Tutor> resultado = strategy.filtrar(List.of(tutor), criterioParaBloque(8, 9));

        assertTrue(resultado.isEmpty());
    }

    @Test
    void filtrarExcluyeTutorQueNoImparteLaMateria() {
        Tutor otro = new Tutor("Ana", "Diaz", "a@mail.com", "2");

        List<Tutor> resultado = strategy.filtrar(List.of(otro), criterioParaBloque(8, 9));

        assertTrue(resultado.isEmpty());
    }

    @Test
    void filtrarSinCriterioCompletoRetornaLaListaSinCambios() {
        List<Tutor> tutores = List.of(tutor);
        criterioDeBusqueda criterios = new criterioDeBusqueda(null, null, null, null, null);

        assertEquals(tutores, strategy.filtrar(tutores, criterios));
    }
}
