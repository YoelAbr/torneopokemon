package org.example.poketorneofuncional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ControladorEquipo implements Initializable {

    @FXML
    private ComboBox<String> comboPokemon;
    @FXML
    private VBox pokemon1;
    @FXML
    private VBox pokemon2;
    @FXML
    private VBox pokemon3;
    @FXML
    private VBox pokemon4;
    @FXML
    private VBox pokemon5;
    @FXML
    private VBox pokemon6;

    @FXML private Button meterpokemon;
    @FXML private Button sacarpokemon;


    public void cambiarInterfaz(String fxml, Button botonOrigen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) botonOrigen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("Error al cargar: " + fxml);
            e.printStackTrace();
        }
    }
    @FXML
    private Button atras;
    @FXML
    public void irAtras() {
        cambiarInterfaz("HubUsuario.fxml", atras);
    }




    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("Poketorneo"); // <-- tu persistence-unit
    private static final EntityManager em = emf.createEntityManager();


    // ==========================================================
// OBTENER O CREAR EQUIPO CON id_equipo = id_usuario
// ==========================================================
    private Integer getIdEquipoActual() {

        try {
            // 1. Obtener el último usuario logueado
            String sqlUltimoLog = "SELECT id_usuario FROM LOGS_ACCESO ORDER BY fecha_hora_login DESC LIMIT 1";
            Integer idUsr = ((Number) em.createNativeQuery(sqlUltimoLog).getSingleResult()).intValue();

            // 2. Buscar si ya tiene equipo
            String sqlEquipo = "SELECT id_equipo FROM EQUIPOS_CHAMPIONS WHERE id_usuario = ?1 LIMIT 1";
            List<?> lista = em.createNativeQuery(sqlEquipo)
                    .setParameter(1, idUsr)
                    .getResultList();

            // 3. Si existe, devolverlo
            if (!lista.isEmpty()) {
                return ((Number) lista.get(0)).intValue();
            }

            // 4. Si NO existe, crearlo con id_equipo = id_usuario
            em.getTransaction().begin();

            String sqlInsert = """
            INSERT INTO EQUIPOS_CHAMPIONS (id_equipo, id_usuario)
            VALUES (?1, ?1)
        """;

            em.createNativeQuery(sqlInsert)
                    .setParameter(1, idUsr)
                    .executeUpdate();

            em.getTransaction().commit();

            System.out.println("Equipo creado automáticamente con ID = " + idUsr);
            return idUsr;

        } catch (Exception e) {
            try { em.getTransaction().rollback(); } catch (Exception ignored) {}
            System.out.println("Error creando equipo: " + e.getMessage());
            return null;
        }
    }



    // MÉTODO QUE SOLO EXTRAE Y RELLENA EL COMBOBOX USANDO JPA
    private void cargarPokemonesEnCombo() {

        // JPQL: selecciona SOLO la columna nombre_pokemon
        List<String> nombres = em.createQuery(
                "SELECT p.nombrePokemon FROM PokedexCompetitiva p", String.class
        ).getResultList();

        comboPokemon.getItems().addAll(nombres);
    }

    //Cajas de texto para que muestre los equipos

    public void cargarEquipoUltimoUsuario() {
        // 1. Obtener el ID del último usuario logueado (Tabla LOGS_ACCESO)
        Integer idUsuario;
        try {
            String sqlUltimoLog = "SELECT id_usuario FROM LOGS_ACCESO ORDER BY fecha_hora_login DESC LIMIT 1";
            idUsuario = ((Number) em.createNativeQuery(sqlUltimoLog).getSingleResult()).intValue();
        } catch (Exception e) {
            System.out.println("No hay registros de inicio de sesión.");
            return;
        }

        // 2. Obtener el ID del equipo asociado a ese usuario (Tabla EQUIPOS_CHAMPIONS)
        Integer idEquipo;
        try {
            String sqlEquipo = "SELECT id_equipo FROM EQUIPOS_CHAMPIONS WHERE id_usuario = ?1 LIMIT 1";
            idEquipo = ((Number) em.createNativeQuery(sqlEquipo)
                    .setParameter(1, idUsuario)
                    .getSingleResult()).intValue();
        } catch (Exception e) {
            System.out.println("El usuario con ID " + idUsuario + " no tiene equipo registrado.");
            return;
        }

        // 3. Obtener nombres de los Pokémon del equipo (Uniendo INTEGRANTES_EQUIPO y POKEDEX_COMPETITIVA)
        List<String> nombres;
        try {
            String sqlPokemons = "SELECT p.nombre_pokemon " +
                    "FROM INTEGRANTES_EQUIPO ie " +
                    "JOIN POKEDEX_COMPETITIVA p ON ie.id_pokedex = p.id_pokedex " +
                    "WHERE ie.id_equipo = ?1 " +
                    "ORDER BY ie.slot";

            // Supresión de advertencias porque sabemos que la base de datos devuelve una lista de Strings
            @SuppressWarnings("unchecked")
            List<String> resultados = em.createNativeQuery(sqlPokemons)
                    .setParameter(1, idEquipo)
                    .getResultList();

            nombres = resultados;
        } catch (Exception e) {
            System.out.println("Error obteniendo los Pokémon: " + e.getMessage());
            nombres = new ArrayList<>();
        }

        // 4. Rellenar con "VACÍO" si el equipo tiene menos de 6 Pokémon para no romper la interfaz
        while (nombres.size() < 6) {
            nombres.add("VACÍO");
        }

        // 5. Recorrer las cajas visuales y aplicar los nombres
        VBox[] cajasTemporales = {pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6};

        for (int i = 0; i < 6; i++) {
            cajasTemporales[i].getChildren().clear();

            Label label = new Label(nombres.get(i));
            label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            cajasTemporales[i].getChildren().add(label);
        }
    }

    //esta wea lo que hace es que el codigo anterior se ejecute
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carga el menú desplegable
        cargarPokemonesEnCombo();

        // ¡AÑADIDO! Automáticamente carga el equipo en las cajas al abrir la pantalla
        cargarEquipoUltimoUsuario();
    }

    //Ahora para que funciones los botones




    // ==========================================================
// BOTÓN: AÑADIR POKÉMON AL EQUIPO
// ==========================================================
    @FXML
    protected void onBotonAñadirClick() {

        String pokemonSeleccionado = comboPokemon.getValue();
        if (pokemonSeleccionado == null) {
            System.out.println("Selecciona un Pokémon primero.");
            return;
        }

        Integer idEquipo = getIdEquipoActual();
        if (idEquipo == null) {
            System.out.println("No se encontró equipo.");
            return;
        }

        em.getTransaction().begin();
        try {
            // 1. Obtener slot máximo
            String sqlSlot = "SELECT COALESCE(MAX(slot), 0) FROM INTEGRANTES_EQUIPO WHERE id_equipo = ?1";
            int maxSlot = ((Number) em.createNativeQuery(sqlSlot)
                    .setParameter(1, idEquipo)
                    .getSingleResult()).intValue();

            if (maxSlot >= 6) {
                System.out.println("El equipo ya está lleno (6 Pokémon).");
                em.getTransaction().rollback();
                return;
            }

            // 2. Obtener id_pokedex del Pokémon seleccionado
            String sqlPokedex = "SELECT id_pokedex FROM POKEDEX_COMPETITIVA WHERE nombre_pokemon = ?1";
            int idPokedex = ((Number) em.createNativeQuery(sqlPokedex)
                    .setParameter(1, pokemonSeleccionado)
                    .getSingleResult()).intValue();

            // 3. Insertar nuevo integrante
            String sqlInsert = """
            INSERT INTO INTEGRANTES_EQUIPO (id_equipo, id_pokedex, slot)
            VALUES (?1, ?2, ?3)
        """;

            em.createNativeQuery(sqlInsert)
                    .setParameter(1, idEquipo)
                    .setParameter(2, idPokedex)
                    .setParameter(3, maxSlot + 1)
                    .executeUpdate();

            em.getTransaction().commit();

            // 4. Actualizar interfaz
            cargarEquipoUltimoUsuario();

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error al añadir Pokémon: " + e.getMessage());
        }
    }

    // ==========================================================
// BOTÓN: ELIMINAR ÚLTIMO POKÉMON DEL EQUIPO
// ==========================================================
    @FXML
    protected void onBotonEliminarClick() {

        Integer idEquipo = getIdEquipoActual();
        if (idEquipo == null) {
            System.out.println("No se encontró equipo.");
            return;
        }

        em.getTransaction().begin();
        try {
            // 1. Obtener último slot ocupado
            String sqlSlot = "SELECT COALESCE(MAX(slot), 0) FROM INTEGRANTES_EQUIPO WHERE id_equipo = ?1";
            int maxSlot = ((Number) em.createNativeQuery(sqlSlot)
                    .setParameter(1, idEquipo)
                    .getSingleResult()).intValue();

            if (maxSlot == 0) {
                System.out.println("El equipo está vacío.");
                em.getTransaction().rollback();
                return;
            }

            // 2. Eliminar el Pokémon del último slot
            String sqlDelete = "DELETE FROM INTEGRANTES_EQUIPO WHERE id_equipo = ?1 AND slot = ?2";

            em.createNativeQuery(sqlDelete)
                    .setParameter(1, idEquipo)
                    .setParameter(2, maxSlot)
                    .executeUpdate();

            em.getTransaction().commit();

            // 3. Actualizar interfaz
            cargarEquipoUltimoUsuario();

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error al eliminar Pokémon: " + e.getMessage());
        }
    }






}

