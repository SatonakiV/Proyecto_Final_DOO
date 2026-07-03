package View;

import Model.entidades.Reserva;
import Model.enums.diaSemana;
import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.List;

public class PanelCalendario extends JPanel implements modelObserver {

    private controller.CalendarioController controller;
    private CalendarioCanvas canvas;
    private JComboBox<String> cbFiltro;
    private JButton btnActualizar;

    public PanelCalendario(controller.CalendarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        inicializarFiltros();
        inicializarCanvas();
        cargarDatos();
    }

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

    private void inicializarCanvas() {
        canvas = new CalendarioCanvas();
        JScrollPane scroll = new JScrollPane(canvas);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        List<Reserva> todasLasReservas = controller.obtenerTodasLasReservas();
        canvas.setReservas(todasLasReservas);
        canvas.repaint();
    }

    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {
        if (evento == eventoModelo.RESERVA_CREADA || evento == eventoModelo.RESERVA_CANCELADA) {
            cargarDatos();
        }
    }
}