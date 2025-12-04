package Usuarios;

import Enumerados.Centros;

public class AdminCentroSalud extends Usuario{

    private Centros centro;

    public AdminCentroSalud(String DNI , long CIPA , Centros centro){
        super(DNI,CIPA);
        this.centro = centro;
    }

    public void setNombre(Paciente p , String nombre){
        p.setNombre(nombre);
    }

    public void setDireccion(Paciente p , String direccion){
        p.setDireccion(direccion);
    }

    public void setDNI(Paciente p , String dni){
        p.setDNI(dni);
    }

    public void setCIPA(Paciente p , long cipa){
        p.setCIPA(cipa);
    }



}
