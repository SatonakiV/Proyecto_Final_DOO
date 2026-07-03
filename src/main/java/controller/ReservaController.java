package controller;

import Model.entidades.BloqueHorario;
import Model.entidades.Estudiante;
import Model.entidades.Materia;
import Model.entidades.Tutor;
import Model.entidades.Reserva;
import java.util.List;
import Model.services.ReservaService;
import Model.Exceptions.cupoMaximoException;
import Model.Exceptions.conflictoHorarioException;
import Model.Exceptions.datosInvalidosException;
import Model.Exceptions.tutorNoDisponibleException;
import Model.services.BuscadorDisponibilidad;
import Model.Strategy.criterioDeBusqueda;

public class ReservaController {

    private ReservaService srvReserva;
    private BuscadorDisponibilidad buscador;

    public ReservaController(ReservaService srvReserva, BuscadorDisponibilidad buscador) {
        this.srvReserva = srvReserva;
        this.buscador = buscador;
    }

    public String agendarClase(Estudiante estudiante, Tutor tutor, Materia materia, BloqueHorario bloque, String notas) {
        try {
            srvReserva.crearReserva(tutor, estudiante, materia, bloque, notas);
            return "Guardado";
        } catch (cupoMaximoException e) {
            return "Sin cupos disponibles.";
        } catch (conflictoHorarioException e) {
            return "Error, horario ya tomado.";
        } catch (tutorNoDisponibleException e) {
            return "Profesor no disponible.";
        } catch (datosInvalidosException e) {
            return "Datos inválidos.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error general";
        }
    }

    public String anular(String idReserva) {
        try {
            srvReserva.cancelarReserva(idReserva);
            return "Reserva anulada con éxito.";
        } catch (Exception e) {
            System.out.println("Error al anular: " + e.getMessage());
            return "Error al anular la reserva.";
        }
    }

    public List<Reserva> obtenerTodas() {
        return srvReserva.obtenerTodas();
    }

    public List<Tutor> buscarTutoresDisponibles(String materia, Model.enums.diaSemana dia, java.time.LocalTime inicio, java.time.LocalTime fin) {
        criterioDeBusqueda criterios = new criterioDeBusqueda(materia, dia, inicio, fin, null);
        buscador.limpiarStrategy();
        buscador.agregarStrategy(new Model.Strategy.BusquedaPorDisponibilidadDeCupo(srvReserva));
        return buscador.buscarTutores(criterios);
    }
}