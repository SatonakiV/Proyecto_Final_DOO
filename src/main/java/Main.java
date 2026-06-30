import View.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame ventana = new MainFrame();
                ventana.setVisible(true);
            }
        });
    }

}
