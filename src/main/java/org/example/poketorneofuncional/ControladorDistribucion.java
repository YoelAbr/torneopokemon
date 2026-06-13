package org.example.poketorneofuncional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ControladorDistribucion implements Initializable {

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

    @FXML
    private VBox entrenador1;
    @FXML
    private VBox entrenador2;
    @FXML
    private VBox entrenador3;
    @FXML
    private VBox entrenador4;
    @FXML
    private VBox entrenador5;
    @FXML
    private VBox entrenador6;
    @FXML
    private VBox entrenador7;

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("Poketorneo"); // <-- tu persistence-unit
    private static final EntityManager em = emf.createEntityManager();

    // ==========================================================
// MÉTODO AUXILIAR PARA MOSTRAR TEXTO EN UNA VBOX
// ==========================================================
    private void setTextoEnVBox(VBox box, String texto) {
        box.getChildren().clear();
        Label label = new Label(texto);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        box.getChildren().add(label);
    }

    public void cargarJugadoresDePartidos() {

        try {
            List<Object[]> lista = em.createNativeQuery(
                    "SELECT id_jugador1, id_jugador2 FROM PARTIDOS ORDER BY ronda"
            ).getResultList();

            if (lista.isEmpty()) {
                System.out.println("No hay partidos registrados.");
                return;
            }

            List<String> nombres = new ArrayList<>();

            for (Object[] fila : lista) {

                Integer id1 = ((Number) fila[0]).intValue();
                Integer id2 = ((Number) fila[1]).intValue();

                String nombre1 = em.createQuery(
                        "SELECT j.nombreEntrenador FROM Jugadore j WHERE j.id = :id",
                        String.class
                ).setParameter("id", id1).getSingleResult();

                String nombre2 = em.createQuery(
                        "SELECT j.nombreEntrenador FROM Jugadore j WHERE j.id = :id",
                        String.class
                ).setParameter("id", id2).getSingleResult();

                nombres.add(nombre1);
                nombres.add(nombre2);
            }

            // Debug para ver el orden real
            System.out.println("Nombres en orden:");
            for (int i = 0; i < nombres.size(); i++) {
                System.out.println(i + " -> " + nombres.get(i));
            }

            // Aseguramos mínimo 7 posiciones
            while (nombres.size() < 7) {
                nombres.add("VACÍO");
            }

            // Asignación al bracket (ajustable)
            setTextoEnVBox(entrenador7, nombres.get(0)); // FINAL / arriba
            setTextoEnVBox(entrenador6, nombres.get(1)); // SEMI izquierda
            setTextoEnVBox(entrenador5, nombres.get(2)); // SEMI derecha
            setTextoEnVBox(entrenador4, nombres.get(3)); // CUARTOS 1
            setTextoEnVBox(entrenador2, nombres.get(4)); // CUARTOS 2
            setTextoEnVBox(entrenador1, nombres.get(5)); // CUARTOS 3
            setTextoEnVBox(entrenador3, nombres.get(6)); // CUARTOS 4

        } catch (Exception e) {
            System.out.println("Error cargando jugadores: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarJugadoresDePartidos();
    }

}
