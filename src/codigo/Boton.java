/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Alejandro Luna Gómez
 */
public class Boton extends JButton {

    //Variables
    private int mina = 0;
    private int i = 0;
    private int j = 0;
    private int numeroMinasAlrededor = 0;
    private boolean noAccionado = true;
    private boolean abanderado = false;

    public Boton(int _i, int _j) {
        i = _i;
        j = _j;
        this.setFocusPainted(false);
        this.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }

    public boolean isAbanderado() {
        return abanderado;
    }

    public void setAbanderado(boolean abanderado) {
        this.abanderado = abanderado;
    }
    
    public boolean isNoAccionado() {
        return noAccionado;
    }

    public void setNoAccionado(boolean noAccionado) {
        this.noAccionado = noAccionado;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getMina() {
        return mina;
    }

    public void setMina(int mina) {
        this.mina = mina;
    }

    public int getNumeroMinasAlrededor() {
        return numeroMinasAlrededor;
    }

    public void setNumeroMinasAlrededor(int numeroMinasAlrededor) {
        this.numeroMinasAlrededor = numeroMinasAlrededor;
    }
    
    
    public void setImagen(int num){   
        String nombre = "";
     
        switch (num){
            case 0: nombre = "celdapress"; break;
            case 1: nombre = "1"; break;
            case 2: nombre = "2"; break;
            case 3: nombre = "3"; break;
            case 4: nombre = "4"; break;
            case 5: nombre = "5"; break;
            case 6: nombre = "6"; break;
            case 7: nombre = "7"; break;
            case 8: nombre = "8"; break;
            case 9: nombre = "bandera"; break;
            case 10: nombre = "celda"; break;
            
        }
        String ruta = "/img/" + nombre + ".png";
        this.setIcon(new ImageIcon(getClass().getResource(ruta)));
    }
}
