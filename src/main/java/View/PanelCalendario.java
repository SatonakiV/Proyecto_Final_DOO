package View;

import Model.entidades.Reserva;
import Model.enums.diaSemana;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.List;

/**
 * Contenedor del calendario semanal, con los controles de filtro y actualización.
 * Delega el dibujado propiamente dicho en CalendarioCanvas. Se refresca solo cuando
 * el Modelo notifica un cambio relevante sobre reservas.
 */
public class PanelCalendario extends JPanel implements modelObserver {

    private controller.CalendarioController controller;
    private CalendarioCanvas canvas;
    private JComboBox<String> cbFiltro;
    private JButton btnActualizar;

    /**
     * @param controller controlador al que se delegan las operaciones sobre el calendario
     */
    public PanelCalendario(controller.CalendarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        inicializarFiltros();
        inicializarCanvas();
        cargarDatos();
    }

    /**
     * Crea el combo de filtro y el botón de actualizar calendario.
     */
    private void inicializarFiltros() {
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.setBorder(BorderFactory.createTitledBorder("Vista del Calendario"));

        cbFiltro = new JComboBox<>(new String[]{"Vista General"});
        cbFiltro.setPreferredSize(new Dimension(200, 25));
        btnActualizar = new JButton("Ver Calendario");

        btnActualizar.addActionListener(e -> cargarDatos());

        panelNorte.add(new JLabel("Filtrar por:"));
        panelNorte.add(cbFiltro);
        panelNorte.add(btnActualizar);
        add(panelNorte, BorderLayout.NORTH);
    }

    /**
     * Crea el canvas del calendario dentro de un panel con scroll.
     */
    private void inicializarCanvas() {
        canvas = new CalendarioCanvas();
        JScrollPane scroll = new JScrollPane(canvas);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Obtiene todas las reservas activas y le pide al canvas que las vuelva a dibujar.
     */
    private void cargarDatos() {
        List<Reserva> todasLasReservas = controller.obtenerTodasLasReservas();
        canvas.setReservas(todasLasReservas);
        canvas.repaint();
    }

    /**
     * @param evento tipo de evento ocurrido
     * @param datos objeto afectado por el evento
     */
    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {
        if (evento == eventoModelo.RESERVA_CREADA || evento == eventoModelo.RESERVA_CANCELADA) {
            cargarDatos();
        }
    }
}