package Model.Strategy;

import Model.entidades.BloqueHorario;
import Model.entidades.Tutor;
import Model.enums.diaSemana;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusquedaPorHorarioTest {

    @Test
    void filtrarDejaSoloLosTutoresDisponiblesEnElRango() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        juan.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        Tutor ana = new Tutor("Ana", "Diaz", "a@mail.com", "2");
        ana.agregarBloqueDisponibilidad(new BloqueHorario(diaSemana.MARTES, LocalTime.of(8, 0), LocalTime.of(12, 0)));

        BusquedaPorHorario strategy = new BusquedaPorHorario();
        criterioDeBusqueda criterios = new criterioDeBusqueda(null, diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0), null);

        List<Tutor> resultado = strategy.filtrar(List.of(juan, ana), criterios);

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void filtrarSinCriterioDeHorarioRetornaLaListaSinCambios() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        List<Tutor> tutores = List.of(juan);

        BusquedaPorHorario strategy = new BusquedaPorHorario();
        criterioDeBusqueda criterios = new criterioDeBusqueda(null, null, null, null, null);

        assertEquals(tutores, strategy.filtrar(tutores, criterios));
    }
}
