package View;

import Model.services.*;
import controller.CalendarioController;
import controller.EstudianteController;
import controller.ReservaController;
import controller.TutorController;

import javax.swing.*;

public class MainFrame extends JFrame {

   private JTabbedPane tabbedPane;

   private PanelTutores panelTutores;
   private PanelReservas panelReservas;
   private PanelEstudiantes panelEstudiantes;
   private PanelCalendario panelCalendario;

   private TutorController tutorController;
   private EstudianteController estudianteController;
   private ReservaController reservaController;
   private CalendarioController calendarioController;

   private TutorService tutorService;
   private EstudianteService estudianteService;
   private ReservaService reservaService;
   private CalendarioService calendarioService;
   private BuscadorDisponibilidad buscadorDisponibilidad;


   public MainFrame() {
       setTitle("Sistema de Gestión de Tutorías");
       setSize(1200, 800);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


       inicializarControladores();
       inicializarVistas();
       conectarVistasAlModelo();

       add(tabbedPane);

       setLocationRelativeTo(null);


   }



    private void inicializarControladores() {
       this.tutorService = new TutorService();
       this.reservaService = new ReservaService();
       this.estudianteService = new EstudianteService();

       this.calendarioService = new CalendarioService(reservaService);
       this.buscadorDisponibilidad = new BuscadorDisponibilidad(tutorService);

       this.tutorController = new TutorController(tutorService, this);
       this.estudianteController = new EstudianteController(estudianteService);
       this.reservaController = new ReservaController(reservaService);

       this.calendarioController = new CalendarioController(calendarioService);

   }

    private void inicializarVistas() {

       this.tabbedPane = new JTabbedPane();


       this.panelTutores = new PanelTutores(tutorController);
       this.panelReservas = new PanelReservas(reservaController);
       this.panelEstudiantes = new PanelEstudiantes(estudianteController);
       this.panelCalendario = new PanelCalendario(calendarioController);


       tabbedPane.addTab("Tutores", panelTutores);
       tabbedPane.addTab("Estudiantes", panelEstudiantes);
        tabbedPane.addTab("Reservas", panelReservas);
       tabbedPane.addTab("Calendario", panelCalendario);
    }

    private void conectarVistasAlModelo() {

       tutorService.agregarObservador(panelEstudiantes);

       estudianteService.agregarObservador(panelEstudiantes);

       reservaService.agregarObservador(panelReservas);
       reservaService.agregarObservador(panelCalendario);

    }
}
