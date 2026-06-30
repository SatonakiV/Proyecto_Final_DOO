package View;

import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;

public class PanelReservas extends JPanel implements modelObserver {


    public PanelReservas(controller.ReservaController controller) {

    }




    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {

    }
}
