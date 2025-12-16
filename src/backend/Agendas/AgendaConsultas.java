package backend.Agendas;

import backend.GestionHistorial.Consulta;
import backend.Medicacion.Medicamento;
import backend.Usuarios.Paciente;

import java.util.ArrayList;
import java.util.List;

public class AgendaConsultas {
    ArrayList<Consulta> consultas; // consultas de todos los usuarios.

    public AgendaConsultas(){
        consultas = new ArrayList<>();
    }

    public void agregarConsulta(Consulta consulta){
        if (!consultas.contains(consulta))consultas.add(consulta);
    }

    public void recetar(Consulta consulta, Medicamento m){
        for (Consulta c : consultas) {
            if (c.equals(consulta)) {
                c.recetarMedicamento(m);
            }
        }
    }


    public void sincronizar(Pacientes lista){
        consultas.clear();
        for (Paciente p : lista.getPacientes()) {
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
