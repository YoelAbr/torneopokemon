package org.example;

import java.util.Scanner;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    try(Scanner teclado = new Scanner (System.in)) {
        String pass = teclado.nextLine();
        String nombre = teclado.nextLine();

        iniciarSesion.comprobarPass(pass);
        iniciarSesion.comprobarUsserName(nombre);
    }catch (Exception e){


    }


    }
}