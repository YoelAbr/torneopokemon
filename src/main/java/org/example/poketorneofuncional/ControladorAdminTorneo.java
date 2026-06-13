package org.example.poketorneofuncional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControladorAdminTorneo {

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




}
