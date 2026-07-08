package Model.services;

import Model.Strategy.BusquedaPorMateria;
import Model.Strategy.BusquedaPorTarifa;
import Model.Strategy.criterioDeBusqueda;
import Model.entidades.Materia;
import Model.entidades.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuscadorDisponibilidadTest {

    private TutorService tutorService;
    private BuscadorDisponibilidad buscador;

    @BeforeEach
    void setUp() {
        tutorService = new TutorService();
        buscador = new BuscadorDisponibilidad(tutorService);

        tutorService.agregarTutor("Juan", "Gomez", "juan@mail.com", "1");
        tutorService.agregarTutor("Ana", "Diaz", "ana@mail.com", "2");
        String idJuan = tutorService.obtenerTodos().get(0).getId();
        String idAna = tutorService.obtenerTodos().get(1).getId();

        tutorService.agregarMateriaATutor(idJuan, new Materia("Calculo", 20, 5));
        tutorService.agregarMateriaATutor(idAna, new Materia("Fisica", 30, 5));
    }

    @Test
    void buscarTutoresSinEstrategiasRetornaTodosLosTutores() {
        criterioDeBusqueda criterios = new criterioDeBusqueda(null, null, null, null, null);

        List<Tutor> resultado = buscador.buscarTutores(criterios);

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarTutoresConUnaEstrategiaFiltraCorrectamente() {
        buscador.agregarStrategy(new BusquedaPorMateria());
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, null);

        List<Tutor> resultado = buscador.buscarTutores(criterios);

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void buscarTutoresEncadenaVariasEstrategias() {
        buscador.agregarStrategy(new BusquedaPorMateria());
        buscador.agregarStrategy(new BusquedaPorTarifa());
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, 10.0);

        List<Tutor> resultado = buscador.buscarTutores(criterios);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void limpiarStrategyEliminaLasEstrategiasAgregadas() {
        buscador.agregarStrategy(new BusquedaPorMateria());
        buscador.limpiarStrategy();
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, null);

        List<Tutor> resultado = buscador.buscarTutores(criterios);

        assertEquals(2, resultado.size());
    }
}
