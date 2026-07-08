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

/**
 * Conecta ReservaService (para crear y anular reservas) con BuscadorDisponibilidad
 * (para buscar tutores), traduciendo cada excepción de negocio en un mensaje de texto
 * distinto para la Vista.
 */
public class ReservaController {

    private ReservaService srvReserva;
    private BuscadorDisponibilidad buscador;

    /**
     * @param srvReserva servicio al que se delegan la creación y cancelación de reservas
     * @param buscador buscador usado para encontrar tutores disponibles
     */
    public ReservaController(ReservaService srvReserva, BuscadorDisponibilidad buscador) {
        this.srvReserva = srvReserva;
        this.buscador = buscador;
    }

    /**
     * @param estudiante estudiante de la reserva
     * @param tutor tutor de la reserva
     * @param materia materia de la reserva
     * @param bloque bloque de horario de la reserva
     * @param notas notas adicionales de la reserva
     * @return un mensaje de éxito, o un mensaje distinto según el tipo de error de negocio ocurrido
     */
    public String agendarClase(Estudiante estudiante, Tutor tutor, Materia materia, BloqueHorario bloque, String notas) {
        try {
            srvReserva.crearReserva(tutor, estudiante, materia, bloque, notas);
            return "Reserva guardada exitosamente.";
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

    /**
     * @param idReserva id de la reserva a anular
     * @return un mensaje de éxito, o un mensaje de error si la operación falla
     */
    public String anular(String idReserva) {
        try {
            srvReserva.cancelarReserva(idReserva);
            return "Reserva anulada con éxito.";
        } catch (Exception e) {
            System.out.println("Error al anular: " + e.getMessage());
            return "Error al anular la reserva.";
        }
    }

    /**
     * @return la lista de todas las reservas, activas o no
     */
    public List<Reserva> obtenerTodas() {
        return srvReserva.obtenerTodas();
    }

    /**
     * Arma el criterio de búsqueda con los parámetros dados y busca tutores usando
     * la estrategia BusquedaPorDisponibilidadDeCupo.
     *
     * @param materia nombre de la materia buscada
     * @param dia día de la semana buscado
     * @param inicio hora de inicio del rango buscado
     * @param fin hora de fin del rango buscado
     * @return la lista de tutores que dictan esa materia y tienen cupo disponible en ese horario
     */
    public List<Tutor> buscarTutoresDisponibles(String materia, Model.enums.diaSemana dia, java.time.LocalTime inicio, java.time.LocalTime fin) {
        criterioDeBusqueda criterios = new criterioDeBusqueda(materia, dia, inicio, fin, null);
        buscador.limpiarStrategy();
        buscador.agregarStrategy(new Model.Strategy.BusquedaPorDisponibilidadDeCupo(srvReserva));
        return buscador.buscarTutores(criterios);
    }
}