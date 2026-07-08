package Model.Strategy;

import Model.entidades.Materia;
import Model.entidades.Tutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusquedaPorTarifaTest {

    @Test
    void filtrarDejaSoloLosTutoresConTarifaMenorOIgualAlMaximo() {
        Tutor barato = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        barato.agregarMateria(new Materia("Calculo", 15, 5));
        Tutor caro = new Tutor("Ana", "Diaz", "a@mail.com", "2");
        caro.agregarMateria(new Materia("Calculo", 50, 5));

        BusquedaPorTarifa strategy = new BusquedaPorTarifa();
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, 20.0);

        List<Tutor> resultado = strategy.filtrar(List.of(barato, caro), criterios);

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void filtrarExcluyeTutoresQueNoImpartenLaMateria() {
        Tutor otraMateria = new Tutor("Ana", "Diaz", "a@mail.com", "2");
        otraMateria.agregarMateria(new Materia("Fisica", 10, 5));

        BusquedaPorTarifa strategy = new BusquedaPorTarifa();
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, 20.0);

        List<Tutor> resultado = strategy.filtrar(List.of(otraMateria), criterios);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void filtrarSinTarifaMaximaRetornaLaListaSinCambios() {
        Tutor juan = new Tutor("Juan", "Gomez", "j@mail.com", "1");
        List<Tutor> tutores = List.of(juan);

        BusquedaPorTarifa strategy = new BusquedaPorTarifa();
        criterioDeBusqueda criterios = new criterioDeBusqueda("Calculo", null, null, null, null);

        assertEquals(tutores, strategy.filtrar(tutores, criterios));
    }
}
