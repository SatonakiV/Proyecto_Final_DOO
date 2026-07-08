package controller;

import Model.entidades.BloqueHorario;
import Model.entidades.Reserva;
import Model.enums.diaSemana;
import Model.services.CalendarioService;

import java.util.List;
import java.util.Map;

/**
 * Traduce cada método de la Vista en una llamada directa al método equivalente de
 * CalendarioService, sin agregar lógica propia. Existe para que la Vista nunca llame
 * directamente a un servicio del Modelo.
 */
public class CalendarioController {
    private CalendarioService calendarioService;

    /**
     * @param calendarioService servicio al que se delegan todas las operaciones
     */
    public CalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    /**
     * @return un mapa con los siete días de la semana como llave y la lista de todas las reservas activas en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerCalendarioGeneral() {
        return calendarioService.obtenerTodasLasReservasSemanal();
    }

    /**
     * @param tutorId id del tutor cuyas reservas se quieren organizar por día
     * @return un mapa con los siete días de la semana como llave y la lista de reservas activas de ese tutor en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerCalendarioPorTutor(String tutorId) {
        return calendarioService.obtenerReservasSemanalPorTutor(tutorId);
    }

    /**
     * @param estudianteId id del estudiante cuyas reservas se quieren organizar por día
     * @return un mapa con los siete días de la semana como llave y la lista de reservas activas de ese estudiante en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerCalendarioPorEstudiante(String estudianteId) {
        return calendarioService.obtenerReservasSemanalPorEstudiante(estudianteId);
    }

    /**
     * @param tutorId id del tutor a revisar
     * @param dia día de la semana a revisar
     * @return la lista de bloques de horario ocupados por reservas activas de ese tutor en ese día
     */
    public List<BloqueHorario> obtenerBloquesOcupados(String tutorId, diaSemana dia) {
        return calendarioService.obtenerBloquesOcupados(tutorId, dia);
    }

    /**
     * @return la lista de todas las reservas activas, sin organizar por día
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return calendarioService.obtenerTodasLasReservas();
    }
}