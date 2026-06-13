package Excepciones;

public class UsserException extends Exception {

    public UsserException(){
        super("usuario o contraseña invalido");
    }

    public UsserException(String mensaje){
        super(mensaje);
    }

    public UsserException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }


}
