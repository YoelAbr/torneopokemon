package Excepciones;

public class PassException extends RuntimeException {

    public PassException(){
        super("contraseña invalido");
    }

    public PassException(String mensaje){
        super(mensaje);
    }

    public PassException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }



}
