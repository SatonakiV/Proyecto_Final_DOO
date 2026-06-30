package View;

import Model.enums.eventoModelo;
import Model.observer.modelObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelTutores extends JPanel implements modelObserver {


    public PanelTutores(controller.TutorController controller) {

    }

    @Override
    public void onModeloActualizado(eventoModelo evento, Object datos) {

    }
}
