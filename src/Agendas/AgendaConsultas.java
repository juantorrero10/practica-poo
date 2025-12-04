package Agendas;

import GestionHistorial.Consulta;
import GestionHistorial.Historial;
import Medicacion.Medicamento;
import Reestricion.Reestricion;
import Usuarios.Paciente;
import Usuarios.Usuario;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

public class AgendaConsultas {
    ArrayList<Consulta> consultas; // consultas de todos los usuarios.

    public AgendaConsultas(){
        consultas = new ArrayList<>();
    }

    public void agregarConsulta(Usuario u, Consulta consulta) throws AccessException {
        Reestricion.noPaciente(u, "AgendaConsultas.agregarConsulta");
        if (!consultas.contains(consulta))consultas.add(consulta);
    }

    public void recetar(Usuario u, Consulta consulta, Medicamento m) throws AccessException {
        Reestricion.medico(u, "AgendaConsultas.preescribir");
        for (Consulta c : consultas) {
            if (c.equals(consulta)) {
                c.recetarMedicamento(u, m);
            }
        }
    }


    public void sincronizar(Usuario u, Pacientes lista) throws AccessException {
        Reestricion.adminCentro(u, "AgendaConsultas.sincronizar");
        consultas.clear();
        for (Paciente p : lista.getPacientes(u)) {
            consultas.addAll(p.getHistorial().getConsultas());
        }
    }

    @Override
    public String toString() {
        return arrayConsultasToString(consultas);
    }

    public static String arrayConsultasToString(List<Consulta> consultas) {
        int i = 0;
        int sz = consultas.size();
        String s = "=======CONSULTAS: ========";
        for (Consulta c : consultas) {
            s += "\nConsulta " + i + " de " + sz + "\n";
            s += c.toString();
            i++;
        }
        return s;
    }



}
