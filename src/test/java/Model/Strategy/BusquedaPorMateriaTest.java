package Model.Strategy;

import Model.entidades.Materia;
import Model.entidades.Tutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusquedaPorMateriaTest {

    @Test
    void filtrarDejaSoloLosTutoresQueImpartenLaMateria() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        juan.agregarMateria(new Materia("Calculo", 20, 5));
        Tutor ana = new Tutor("Ana", "Diaz", "a@mail.com", "2");
        ana.agregarMateria(new Materia("Fisica", 20, 5));

        BusquedaPorMateria strategy = new BusquedaPorMateria();
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, null);

        List<Tutor> resultado = strategy.filtrar(List.of(juan, ana), criterios);

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void filtrarSinMateriaEnElCriterioRetornaLaListaSinCambios() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        Tutor ana = new Tutor("Ana", "Diaz", "a@mail.com", "2");
        List<Tutor> tutores = List.of(juan, ana);

        BusquedaPorMateria strategy = new BusquedaPorMateria();
        criterioDeBusqueda criterios = new criterioDeBusqueda(null, null, null, null, null);

        assertEquals(tutores, strategy.filtrar(tutores, criterios));
    }

    @Test
    void filtrarConMateriaVaciaRetornaLaListaSinCambios() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        List<Tutor> tutores = List.of(juan);

        BusquedaPorMateria strategy = new BusquedaPorMateria();
        criterioDeBusqueda criterios = new criterioDeBusqueda("", null, null, null, null);

        assertEquals(tutores, strategy.filtrar(tutores, criterios));
    }
}
