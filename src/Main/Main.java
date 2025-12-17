package Main;


import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import backend.Agendas.Plantilla;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.Admin;
import backend.Usuarios.AdminCentroSalud;
import backend.Usuarios.Medico;
import Controlador.Controlador;
import interfaz.MainVentana;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        inicializarSistemaArchivos();

        //Inicializacion temporal.
        Plantilla plantilla = new Plantilla();
        AgendaConsultas agendaConsultas = new AgendaConsultas();
        AgendaCitas citas = new AgendaCitas();

        Medico m1 = new Medico("11112222A", 101, Especialidades.CARDIOLOGIA, Centros.HOSPITAL_UNIVERSITARIO_DE_FUENLABRADA);
        Medico m2 = new Medico("33334444Z", 102, Especialidades.PEDIATRIA, Centros.HOSPITAL_UNIVERSITARIO_PRINCIPE_DE_ASTURIAS);
        AdminCentroSalud administrador = new AdminCentroSalud("44556677M", 4001, Centros.HOSPITAL_UNIVERSITARIO_TORREJON);
        Admin admin = new Admin("84754928O", 5000);

        plantilla.agregarMedico(m1);
        plantilla.agregarMedico(m2);
        plantilla.agregarAdministradorCentro(administrador);
        plantilla.agregarAdministrador(admin);

        Controlador c = new Controlador(plantilla, "datos/Pacientes.csv", agendaConsultas, citas);

        Log.INFO("Usuarios en el sistema: ");
        System.out.println(c.stringGetUsuarios());

        // Iniciar Interfaz gráfica.
        new MainVentana(c).setVisible(true);

    }

    private static void inicializarSistemaArchivos() {

        Path dataDir = Paths.get("datos");
        Path historialesDir = dataDir.resolve("Historiales");

        Path pacientes = dataDir.resolve("Pacientes.csv");
        Path plantilla = dataDir.resolve("Plantilla.csv");
        Path citas = dataDir.resolve("Citas.csv");

        Log.INFO("Iniciando sistema de ficheros");

        try {
            // Crear carpeta datos/
            Files.createDirectories(dataDir);

            // Crear carpeta Historiales/
            Files.createDirectories(historialesDir);

            // Crear CSV si no existen
            crearCSVSiNoExiste(pacientes);
            crearCSVSiNoExiste(plantilla);
            crearCSVSiNoExiste(citas);

        } catch (IOException e) {
            Log.ERR("Error inicializando el sistema de ficheros: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void crearCSVSiNoExiste(Path fichero) throws IOException {
        if (Files.notExists(fichero)) {
            Files.createFile(fichero);
        }
    }
}