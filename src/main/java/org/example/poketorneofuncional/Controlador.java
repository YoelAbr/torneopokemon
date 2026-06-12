package org.example.poketorneofuncional;

import Excepciones.PassException;
import Excepciones.UsserException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
// He quitado el Label porque no tienes ninguno en tu pantalla

public class Controlador {

    // 1. Apuntamos EXACTAMENTE a los fx:id de tu FXML
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    // 2. Tus variables para guardar la información
    private String Usser;
    private String Pass;


    // 3. El método que llamará el botón
    @FXML
    protected void onBotonGuardarClick() {
        // Sacamos el texto visual y lo guardamos en tus variables de Java
        Usser = txtUsuario.getText();
        Pass = txtPassword.getText();

        try{
            iniciarSesion.comprobarUsserName(Usser);
            iniciarSesion.comprobarPass(Pass);

        }catch (UsserException e){
            System.err.println(e.getMessage());
        }catch (PassException e){
            System.err.println(e.getMessage());
        }

        if (Usser.equals("admin_torneo")){

            try {
                // 1. Cargamos el nuevo archivo FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HubAdmin.fxml"));
                Scene escenaHub = new Scene(fxmlLoader.load());
                // 2. Obtenemos la ventana actual usando cualquier elemento de la interfaz (como txtUsuario)
                Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
                // 3. Cambiamos la vista a la nueva
                ventanaActual.setScene(escenaHub);
            } catch (IOException e) {
                System.out.println("Error al cargar la ventana HubAdmin");
                e.printStackTrace();
            }

        }else{

            try {
                // 1. Cargamos el nuevo archivo FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HubUsuario.fxml"));
                Scene escenaHub = new Scene(fxmlLoader.load());
                // 2. Obtenemos la ventana actual usando cualquier elemento de la interfaz (como txtUsuario)
                Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
                // 3. Cambiamos la vista a la nueva
                ventanaActual.setScene(escenaHub);
            } catch (IOException e) {
                System.out.println("Error al cargar la ventana HubUsuario");
                e.printStackTrace();
            }


        }

        // Lo imprimimos en consola para confirmar
        System.out.println("Usuario capturado: " + Usser);
        System.out.println("Contraseña capturada: " + Pass);
        iniciarSesion.registrarLogDeAcceso(Usser);

    }

}
