package Usuarios;

import java.util.ArrayList;




public class ListaPaciente {
    private static ArrayList <Paciente> listaPaciente;


    public ListaPaciente(){
        listaPaciente = new ArrayList <>();
    }

    public static boolean añadirListaPaciente(Paciente p){
        if (p==null || listaPaciente.contains(p)) return false;
        listaPaciente.add(p);
        return true;
    }
}
