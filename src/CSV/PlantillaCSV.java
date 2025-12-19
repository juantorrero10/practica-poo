package CSV;

import Controlador.TipoUsuario;
import Main.Log;
import backend.Agendas.Plantilla;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.Admin;
import backend.Usuarios.AdminCentroSalud;
import backend.Usuarios.Medico;

import java.io.IOException;
import java.util.ArrayList;

public class PlantillaCSV extends ProcesadorCSV {

    public PlantillaCSV( String Ruta) { super(Ruta); }

    /**
     * Campos del CSV:
     * Tipo             <String>:          "Admin", "AdminCentro", "Médico"
     * DNI              <String>
     * CIPA             <long>
     * Especialidad:    <Especialidades>:  NULL -> "NO_ESPECIFICADO"
     * Centro:          <Centros>:         NULL -> "NO_ESPECIFICADO"
     */

    public boolean exportarPlantilla(Plantilla p) throws IOException {

        ArrayList<String[]> cont = new ArrayList<>();
        for(Medico m : p.getMedicos()) {
            cont.add( new String[] {
                    m.getTipoUsuario().toString(),
                    m.getDNI(),
                    ((Long)m.getCIPA()).toString(),
                    m.getEspecialidad().toString(),
                    m.getCentro().toString(),
            });
        }

        for (AdminCentroSalud ac : p.getAdministradoresCentroSalud()) {
            cont.add( new String[] {
                    ac.getTipoUsuario().toString(),
                    ac.getDNI(),
                    ((Long)ac.getCIPA()).toString(),
                    Especialidades.NO_ESPECIFICADO.toString(),
                    ac.getCentro().toString(),
            });
        }

        for (Admin ac : p.getAdministradores()) {
            cont.add( new String[] {
                    ac.getTipoUsuario().toString(),
                    ac.getDNI(),
                    ((Long)ac.getCIPA()).toString(),
                    Especialidades.NO_ESPECIFICADO.toString(),
                    Centros.NO_ESPECIFICADO.toString(),
            });
        }

        return exportarCSV(cont);
    }
    public Plantilla importarPlantilla() {
        ArrayList<String[]> contenido = importarCSV();
        ArrayList<Medico> arrMedicos = new ArrayList<>();
        ArrayList<AdminCentroSalud> arrAdCentro = new ArrayList<>();
        ArrayList<Admin> arrAdmin = new ArrayList<>();

        for (String[] fila : contenido) {
            switch (TipoUsuario.fromString(fila[0])) {
                case ADMIN:
                    arrAdmin.add(new Admin(
                            fila[1],
                            Long.parseLong(fila[2])
                        ));
                    break;
                case ADMINCENTRO:
                    arrAdCentro.add(new AdminCentroSalud(
                            fila[1],
                            Long.parseLong(fila[2]),
                            Centros.valueOf(fila[4])
                        ));
                    break;
                case MEDICO:
                    arrMedicos.add(new Medico(
                            fila[1],
                            Long.parseLong(fila[2]),
                            Especialidades.valueOf(fila[3]),
                            Centros.valueOf(fila[4])
                    ));
                    break;
                default:
                    Log.ERR("Tipo de Usuario invalido en Plantilla.csv");
            }
        }

        return new Plantilla(arrAdmin, arrMedicos, arrAdCentro);
    }

}
