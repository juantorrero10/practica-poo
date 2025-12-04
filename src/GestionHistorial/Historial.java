package GestionHistorial;
import Medicacion.Medicamento;
import Pruebas.Imagen;
import Pruebas.Laboratorio;
import Pruebas.Prueba;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    private List<Consulta> consultas;
    private List<Imagen> pruebasImagen;
    private List<Laboratorio> pruebasLaboratorio;

    public Historial() {
        consultas = new ArrayList<>();
        pruebasImagen = new ArrayList<>();
        pruebasLaboratorio = new ArrayList<>();
    }


    /**
     * @return lista de medicamentos activos.
     */
    public List<Medicamento> getMedicamentosActivos() {
        ArrayList<Medicamento> ret = new ArrayList<>();

        for (Consulta c :  consultas) {
            if (c.getPreescripcion().estaActivo()) {
                ret.add(c.getPreescripcion());
            }
        }

        return ret;
    }

    // ############################## apartado Pruebas ##################################

    public void agregarImagen(Imagen pi) {
        pruebasImagen.add(pi);
    }
    public void agregarLabotorio(Laboratorio pl) {
        pruebasLaboratorio.add(pl);
    }
    public void agregarConsulta(Consulta c) {consultas.add(c);}

    public List<Imagen> getImagen() {
        return pruebasImagen;
    }

    public List<Laboratorio> getLaboratorio() {
        return pruebasLaboratorio;
    }
}