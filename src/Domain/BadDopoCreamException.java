package Domain;

/**
 * Clase de excepciones del juego Bad Dopo-Cream
 */
public class BadDopoCreamException extends Exception {
    
    /**
     * Constructor que recibe un mensaje de error
     */
    public BadDopoCreamException(String message) {
        super(message);
    }
    
    /**
     * Constructor que recibe un mensaje y la causa original del error
     */
    public BadDopoCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}