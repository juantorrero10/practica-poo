package Usuarios;

import Enumeradores.Centros;

public class AdminCentroSalud extends Usuario{

    private Centros centro;

    public AdminCentroSalud(String DNI , long CIPA , Centros centro){
        super(DNI,CIPA);
        this.centro = centro;
    }

    public Centros getCentro() {
        return centro;
    }



}
