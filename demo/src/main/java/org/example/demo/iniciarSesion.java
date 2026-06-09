package org.example.demo;
import Excepciones.PassException;
import Excepciones.UsserException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


public class iniciarSesion {

    public static void comprobarUsserName (String nombre) throws UsserException {

        //agarrar los nombres y meterlos en un Array

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Poketorneo");
        EntityManager em = emf.createEntityManager();

        List<String> lista = em.createQuery(
                "SELECT u.username FROM Usuario u", String.class
        ).getResultList();

        String[] array = lista.toArray(new String[0]);

        //combrobar si el nombre anda en la base de datos

        int correctas = 0;

        for (int i = 0; i < array.length; i++) {

            if(array[i].equals(nombre)){
                correctas++;
            }

        }

        if (correctas == 0){
            throw new UsserException("usuario incorrecto");
        }

        System.out.println("nombre de usuario correcto");

    }

    public static void comprobarPass (String pass) throws PassException {
        //acá necesito hacer un metodo para comprobar la contraseña

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String passHash = encoder.encode(pass);
        // boolean ok = encoder.matches(passIntroducido, hashGuardado); (esto es para comprobar si el hash es igual al otro)
        //con este codigo metemos una columna en un Array para poder comprobar

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Poketorneo");
        EntityManager em = emf.createEntityManager();

        List<String> lista = em.createQuery(
                "SELECT u.passwordHash FROM Usuario u", String.class
        ).getResultList();

        String[] array = lista.toArray(new String[0]);

        //ahora metemos el codigo para revisar la contraseña

        int correctas = 0;

        for (int i = 0; i < array.length; i++) {

            if(encoder.matches(pass, array[i])){
                correctas++;
            }
            System.out.println(passHash);

        }
        if (correctas == 0){
            throw new PassException("contraseña incorrecta");
        }

        System.out.println("contraseña correcta");

    }


}
