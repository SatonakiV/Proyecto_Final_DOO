package Model.services;

import Model.entidades.BloqueHorario;
import Model.entidades.Reserva;
import Model.enums.diaSemana;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CalendarioService {
    private ReservaService reservaService;

    // Recibe ReservaService mediante inyección de dependencias
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

    public Map<diaSemana, List<Reserva>> obtenerReservasSemanalPorTutor(String tutorId) {
        Map<diaSemana, List<Reserva>> mapa = inicializarMapaSemanal();
        List<Reserva> reservasTutor = reservaService.obtenerReservasActivasPorTutor(tutorId);

        for (Reserva r : reservasTutor) {
            mapa.get(r.getBloqueHorario().getDia()).add(r);
        }
        return mapa;
    }

    public Map<diaSemana, List<Reserva>> obtenerReservasSemanalPorEstudiante(String estudianteId) {
        Map<diaSemana, List<Reserva>> mapa = inicializarMapaSemanal();
        List<Reserva> reservasEstudiante = reservaService.obtenerReservasActivasPorEstudiante(estudianteId);

        for (Reserva r : reservasEstudiante) {
            mapa.get(r.getBloqueHorario().getDia()).add(r);
        }
        return mapa;
    }

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