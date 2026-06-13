package org.example.poketorneofuncional;
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


        }
        if (correctas == 0){
            throw new PassException("contraseña incorrecta");
        }

        System.out.println("contraseña correcta");

    }

    public static void registrarLogDeAcceso(String nombreUsuario) {
        // 1. Comprobamos si la variable llega vacía antes de hacer nada
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            System.out.println("❌ Error: El nombre de usuario está vacío o es nulo.");
            return;
        }

        System.out.println("⏳ Buscando en la BD al usuario: [" + nombreUsuario + "]");

        try (
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("Poketorneo");
                EntityManager em = emf.createEntityManager()
        ) {
            em.getTransaction().begin();

            // *** CAMBIO IMPORTANTE ***
            // Ahora buscamos la ID REAL del entrenador (tabla JUGADORES)
            String consultaId =
                    "SELECT j.id_usuario " +
                            "FROM JUGADORES j " +
                            "JOIN USUARIOS u ON j.id_usuario = u.id_usuario " +
                            "WHERE u.username = ?1";

            Integer idUsuario = ((Number) em.createNativeQuery(consultaId)
                    .setParameter(1, nombreUsuario)
                    .getSingleResult()).intValue();

            System.out.println("✅ Entrenador encontrado con ID: " + idUsuario + ". Guardando log...");

            // 3. Insertamos el log
            String consultaInsert = "INSERT INTO LOGS_ACCESO (id_usuario) VALUES (?1)";
            em.createNativeQuery(consultaInsert)
                    .setParameter(1, idUsuario)
                    .executeUpdate();

            em.getTransaction().commit();

            System.out.println("✅ Log registrado correctamente en la base de datos.");

        } catch (Exception e) {
            // Si entra aquí, es porque el usuario que escribiste no existe en la tabla USUARIOS/JUGADORES
            System.out.println("❌ Error: No existe el usuario '" + nombreUsuario + "' en la base de datos.");
        }
    }



}
