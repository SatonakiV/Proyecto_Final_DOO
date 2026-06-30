package View;

import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;

public class PanelCalendario extends JPanel implements modelObserver {


    public PanelCalendario(controller.CalendarioController controller) {

    }

    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {

    }
}
