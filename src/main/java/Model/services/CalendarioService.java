package Model.services;

import Model.entidades.BloqueHorario;
import Model.entidades.Reserva;
import Model.enums.diaSemana;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Reorganiza las reservas que ya existen en ReservaService para que sean fáciles de
 * dibujar en un calendario semanal, sin agregar reglas de negocio nuevas.
 */
public class CalendarioService {
    private ReservaService reservaService;

    /**
     * @param reservaService servicio del que se obtienen las reservas a organizar
     */
    public CalendarioService(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * Método auxiliar privado para inicializar un mapa con todos los días de la semana
     * y listas vacías. Esto evita errores de NullPointerException al llenar los datos.
     */
    private Map<diaSemana, List<Reserva>> inicializarMapaSemanal() {
        Map<diaSemana, List<Reserva>> mapa = new EnumMap<>(diaSemana.class);
        for (diaSemana dia : diaSemana.values()) {
            mapa.put(dia, new ArrayList<>());
        }
        return mapa;
    }

    /**
     * @param tutorId id del tutor cuyas reservas se quieren organizar por día
     * @return un mapa con los siete días de la semana como llave y la lista de reservas activas de ese tutor en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerReservasSemanalPorTutor(String tutorId) {
        Map<diaSemana, List<Reserva>> mapa = inicializarMapaSemanal();
        List<Reserva> reservasTutor = reservaService.obtenerReservasActivasPorTutor(tutorId);

        for (Reserva r : reservasTutor) {
            mapa.get(r.getBloqueHorario().getDia()).add(r);
        }
        return mapa;
    }

    /**
     * @param estudianteId id del estudiante cuyas reservas se quieren organizar por día
     * @return un mapa con los siete días de la semana como llave y la lista de reservas activas de ese estudiante en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerReservasSemanalPorEstudiante(String estudianteId) {
        Map<diaSemana, List<Reserva>> mapa = inicializarMapaSemanal();
        List<Reserva> reservasEstudiante = reservaService.obtenerReservasActivasPorEstudiante(estudianteId);

        for (Reserva r : reservasEstudiante) {
            mapa.get(r.getBloqueHorario().getDia()).add(r);
        }
        return mapa;
    }

    /**
     * @return un mapa con los siete días de la semana como llave y la lista de todas las reservas activas en cada día
     */
    public Map<diaSemana, List<Reserva>> obtenerTodasLasReservasSemanal() {
        Map<diaSemana, List<Reserva>> mapa = inicializarMapaSemanal();
        List<Reserva> todas = reservaService.obtenerTodas();

        // Filtramos para incluir únicamente las reservas activas en la vista general
        for (Reserva r : todas) {
            if (r.estaActiva()) {
                mapa.get(r.getBloqueHorario().getDia()).add(r);
            }
        }
        return mapa;
    }

    /**
     * @return la lista de todas las reservas activas, sin organizar por día
     */
    public List<Reserva> obtenerTodasLasReservas() {
        List<Reserva> activas = new ArrayList<>();
        for (Reserva r : reservaService.obtenerTodas()) {
            if (r.estaActiva()) activas.add(r);
        }
        return activas;
    }

    /**
     * @param tutorId id del tutor a revisar
     * @param dia día de la semana a revisar
     * @return la lista de bloques de horario ocupados por reservas activas de ese tutor en ese día
     */
    public List<BloqueHorario> obtenerBloquesOcupados(String tutorId, diaSemana dia) {
        List<BloqueHorario> bloquesOcupados = new ArrayList<>();
        List<Reserva> reservasTutor = reservaService.obtenerReservasActivasPorTutor(tutorId);

        for (Reserva r : reservasTutor) {
            if (r.getBloqueHorario().getDia() == dia) {
                bloquesOcupados.add(r.getBloqueHorario());
            }
        }
        return bloquesOcupados;
    }
}