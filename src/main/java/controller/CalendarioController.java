package Controller;

import Model.entidades.BloqueHorario;
import Model.entidades.Reserva;
import Model.enums.diaSemana;
import Model.services.CalendarioService;

import java.util.List;
import java.util.Map;

public class CalendarioController {
    private CalendarioService calendarioService;

    // Recibe CalendarioService
    public CalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    // Retorna el mapa completo sin filtrar (vista general)
    public Map<diaSemana, List<Reserva>> obtenerCalendarioGeneral() {
        return calendarioService.obtenerTodasLasReservasSemanal();
    }

    // Retorna el mapa filtrado por tutor
    public Map<diaSemana, List<Reserva>> obtenerCalendarioPorTutor(String tutorId) {
        return calendarioService.obtenerReservasSemanalPorTutor(tutorId);
    }

    // Retorna el mapa filtrado por estudiante
    public Map<diaSemana, List<Reserva>> obtenerCalendarioPorEstudiante(String estudianteId) {
        return calendarioService.obtenerReservasSemanalPorEstudiante(estudianteId);
    }

    // Delegación directa para obtener bloques ocupados de un tutor en un día específico
    public List<BloqueHorario> obtenerBloquesOcupados(String tutorId, diaSemana dia) {
        return calendarioService.obtenerBloquesOcupados(tutorId, dia);
    }
}