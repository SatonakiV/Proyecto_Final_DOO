package View;

import Model.enums.diaSemana;
import Model.services.*;
import controller.CalendarioController;
import controller.EstudianteController;
import controller.ReservaController;
import controller.TutorController;

import javax.swing.*;
import java.time.LocalTime;

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
        cargarDatosDePrueba();
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
       this.reservaController = new ReservaController(this.reservaService, this.buscadorDisponibilidad);

       this.calendarioController = new CalendarioController(calendarioService);

   }

    private void inicializarVistas() {

       this.tabbedPane = new JTabbedPane();


       this.panelTutores = new PanelTutores(tutorController);
       this.panelReservas = new PanelReservas(reservaController, estudianteController, tutorController);
       this.panelEstudiantes = new PanelEstudiantes(estudianteController);
       this.panelCalendario = new PanelCalendario(calendarioController);


       tabbedPane.addTab("Tutores", panelTutores);
       tabbedPane.addTab("Estudiantes", panelEstudiantes);
        tabbedPane.addTab("Reservas", panelReservas);
       tabbedPane.addTab("Calendario", panelCalendario);
    }

    private void conectarVistasAlModelo() {

        tutorService.agregarObservador(panelTutores);

        estudianteService.agregarObservador(panelEstudiantes);

       reservaService.agregarObservador(panelReservas);
       reservaService.agregarObservador(panelCalendario);
    }

    private void cargarDatosDePrueba() {
        tutorController.agregarTutor("Carlos", "Mendez", "cmendez@udec.cl", "555-1001");
        tutorController.agregarTutor("Laura", "Rios", "lrios@udec.cl", "555-1002");
        tutorController.agregarTutor("Pedro", "Salas", "psalas@udec.cl", "555-1003");

        java.util.List<Model.entidades.Tutor> tutores = tutorController.obtenerTodos();

        if (tutores.size() >= 1) {
            String id1 = tutores.get(0).getId();
            tutorController.agregarMateriaATutor(id1, "Matematicas", 15.0, 5);
            tutorController.agregarMateriaATutor(id1, "Fisica", 18.0, 4);
            tutorController.agregarBloqueATutor(id1, diaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0));
            tutorController.agregarBloqueATutor(id1, diaSemana.MIERCOLES, LocalTime.of(8, 0), LocalTime.of(12, 0));
        }

        if (tutores.size() >= 2) {
            String id2 = tutores.get(1).getId();
            tutorController.agregarMateriaATutor(id2, "Programacion", 20.0, 6);
            tutorController.agregarMateriaATutor(id2, "Matematicas", 12.0, 3);
            tutorController.agregarBloqueATutor(id2, diaSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(13, 0));
            tutorController.agregarBloqueATutor(id2, diaSemana.JUEVES, LocalTime.of(9, 0), LocalTime.of(13, 0));
        }

        if (tutores.size() >= 3) {
            String id3 = tutores.get(2).getId();
            tutorController.agregarMateriaATutor(id3, "Quimica", 16.0, 4);
            tutorController.agregarBloqueATutor(id3, diaSemana.VIERNES, LocalTime.of(10, 0), LocalTime.of(14, 0));
        }

        estudianteController.registrar("Ana", "Torres", "atorres@udec.cl", "555-2001");
        estudianteController.registrar("Luis", "Gomez", "lgomez@udec.cl", "555-2002");
        estudianteController.registrar("Sofia", "Vera", "svera@udec.cl", "555-2003");
        estudianteController.registrar("Diego", "Castro", "dcastro@udec.cl", "555-2004");
    }
}
