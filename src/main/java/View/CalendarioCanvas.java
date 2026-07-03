package View;

import Model.entidades.Reserva;
import Model.enums.diaSemana;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarioCanvas extends JPanel {

    private List<Reserva> reservas = new ArrayList<>();

    private static final int ANCHO_COLUMNA = 140;
    private static final int ALTO_FILA = 50;
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 40;

    private static final diaSemana[] DIAS_ORDEN = {
            diaSemana.LUNES, diaSemana.MARTES, diaSemana.MIERCOLES,
            diaSemana.JUEVES, diaSemana.VIERNES, diaSemana.SABADO
    };

    private static final Color[] COLORES_RESERVA = {
            new Color(70, 130, 180),
            new Color(60, 179, 113),
            new Color(255, 140, 0),
            new Color(147, 112, 219),
            new Color(220, 90, 90)
    };

    public CalendarioCanvas() {
        setBackground(new Color(245, 245, 250));
        setPreferredSize(new Dimension(OFFSET_X + DIAS_ORDEN.length * ANCHO_COLUMNA + 20, 700));
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarEncabezados(g2);
        dibujarFilasHoras(g2);

        if (reservas == null || reservas.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g2.drawString("No hay reservas para mostrar", OFFSET_X + 20, OFFSET_Y + 80);
            return;
        }

        dibujarReservas(g2);
    }

    private void dibujarEncabezados(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        for (int i = 0; i < DIAS_ORDEN.length; i++) {
            int x = OFFSET_X + i * ANCHO_COLUMNA;
            g2.setColor(new Color(60, 80, 120));
            g2.fillRoundRect(x + 2, 5, ANCHO_COLUMNA - 4, OFFSET_Y - 10, 8, 8);
            g2.setColor(Color.WHITE);
            g2.drawString(DIAS_ORDEN[i].getEtiqueta(), x + 15, OFFSET_Y - 13);
        }
        for (int i = 0; i <= DIAS_ORDEN.length; i++) {
            int x = OFFSET_X + i * ANCHO_COLUMNA;
            g2.setColor(new Color(200, 200, 210));
            g2.drawLine(x, OFFSET_Y, x, OFFSET_Y + 14 * ALTO_FILA);
        }
    }

    private void dibujarFilasHoras(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int hora = 7; hora <= 20; hora++) {
            int y = OFFSET_Y + (hora - 7) * ALTO_FILA;
            g2.setColor(new Color(180, 180, 195));
            g2.drawLine(OFFSET_X, y, OFFSET_X + DIAS_ORDEN.length * ANCHO_COLUMNA, y);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.format("%02d:00", hora), 5, y + 14);
        }
    }

    private void dibujarReservas(Graphics2D g2) {
        int colorIndex = 0;

        for (Reserva r : reservas) {
            if (!r.estaActiva()) continue;

            diaSemana diaReserva = r.getBloqueHorario().getDia();
            int columna = -1;
            for (int i = 0; i < DIAS_ORDEN.length; i++) {
                if (DIAS_ORDEN[i] == diaReserva) {
                    columna = i;
                    break;
                }
            }
            if (columna == -1) continue;

            int horaInicio = r.getBloqueHorario().getHoraInicio().getHour();
            int minInicio  = r.getBloqueHorario().getHoraInicio().getMinute();
            int horaFin    = r.getBloqueHorario().getHoraFin().getHour();
            int minFin     = r.getBloqueHorario().getHoraFin().getMinute();

            int y1     = OFFSET_Y + (horaInicio - 7) * ALTO_FILA + (minInicio * ALTO_FILA / 60);
            int y2     = OFFSET_Y + (horaFin    - 7) * ALTO_FILA + (minFin    * ALTO_FILA / 60);
            int x      = OFFSET_X + columna * ANCHO_COLUMNA + 4;
            int altura = Math.max(y2 - y1, 20);

            Color color = COLORES_RESERVA[colorIndex % COLORES_RESERVA.length];
            g2.setColor(color);
            g2.fillRoundRect(x, y1, ANCHO_COLUMNA - 8, altura, 10, 10);
            g2.setColor(color.darker());
            g2.drawRoundRect(x, y1, ANCHO_COLUMNA - 8, altura, 10, 10);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString(r.getTutor().getNombre(), x + 5, y1 + 14);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString(r.getMateria().getNombre(), x + 5, y1 + 26);

            colorIndex++;
        }
    }
}
