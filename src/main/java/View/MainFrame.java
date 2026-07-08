package View;

import Model.enums.diaSemana;
import Model.services.*;
import controller.CalendarioController;
import controller.EstudianteController;
import controller.ReservaController;
import controller.TutorController;

import javax.swing.*;
import java.time.LocalTime;

/**
 * Ventana principal de la aplicación. Arma en orden los servicios, los controladores,
 * los paneles y las conexiones de Observer entre ellos, y carga datos de ejemplo para
 * no arrancar con las tablas vacías. Es la única clase que conoce y crea todas las
 * piezas del sistema.
 */
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


   /**
    * Crea la ventana principal, inicializando controladores, datos de ejemplo, vistas
    * y las conexiones de Observer entre el Modelo y la Vista.
    */
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

    /**
     * Crea los servicios del Modelo y los controladores que dependen de ellos.
     */
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

    /**
     * Crea el panel con pestañas y los paneles de cada sección de la aplicación.
     */
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

    /**
     * Registra cada panel como observador de su servicio correspondiente, para que se
     * refresquen solos cuando el Modelo notifique un cambio.
     */
    private void conectarVistasAlModelo() {

        tutorService.agregarObservador(panelTutores);

        estudianteService.agregarObservador(panelEstudiantes);

       reservaService.agregarObservador(panelReservas);
       reservaService.agregarObservador(panelCalendario);
    }

    /**
     * Registra tutores y estudiantes de ejemplo, con sus materias y bloques de horario,
     * para que la aplicación no arranque con las tablas vacías.
     */
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

        java.util.List<Model.entidades.Estudiante> estudiantes = estudianteController.obtenerTodos();

        if (tutores.size() >= 3 && estudiantes.size() >= 4) {
            Model.entidades.Tutor carlos = tutores.get(0);
            Model.entidades.Tutor laura = tutores.get(1);
            Model.entidades.Tutor pedro = tutores.get(2);

            Model.entidades.Estudiante ana = estudiantes.get(0);
            Model.entidades.Estudiante luis = estudiantes.get(1);
            Model.entidades.Estudiante sofia = estudiantes.get(2);
            Model.entidades.Estudiante diego = estudiantes.get(3);

            reservaController.agendarClase(ana, carlos, buscarMateria(carlos, "Matematicas"),
                    new Model.entidades.BloqueHorario(diaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                    "Refuerzo de álgebra");

            reservaController.agendarClase(luis, carlos, buscarMateria(carlos, "Fisica"),
                    new Model.entidades.BloqueHorario(diaSemana.LUNES, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                    "Repaso de cinemática");

            reservaController.agendarClase(sofia, laura, buscarMateria(laura, "Programacion"),
                    new Model.entidades.BloqueHorario(diaSemana.MARTES, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                    "Introducción a Java");

            reservaController.agendarClase(diego, laura, buscarMateria(laura, "Matematicas"),
                    new Model.entidades.BloqueHorario(diaSemana.JUEVES, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                    "Preparación para examen");

            reservaController.agendarClase(ana, pedro, buscarMateria(pedro, "Quimica"),
                    new Model.entidades.BloqueHorario(diaSemana.VIERNES, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                    "Laboratorio de química");
        }
    }

    /**
     * @param tutor tutor en el que se busca la materia
     * @param nombre nombre de la materia a buscar
     * @return la materia del tutor con ese nombre, o null si no la dicta
     */
    private Model.entidades.Materia buscarMateria(Model.entidades.Tutor tutor, String nombre) {
        for (Model.entidades.Materia m : tutor.getMaterias()) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                return m;
            }
        }
        return null;
    }
}
