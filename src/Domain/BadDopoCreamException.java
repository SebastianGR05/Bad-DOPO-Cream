package Domain;

/**
 * Clase de excepciones personalizada para el proyecto Bad Dopo-Cream
 * Maneja todos los errores específicos del juego
 */
public class BadDopoCreamException extends Exception {
    
    // Errores de posición y movimiento
    public static final String INVALID_POSITION = "Posición inválida en el tablero";
    public static final String INVALID_MOVE = "Movimiento no permitido";
    public static final String OUT_OF_BOUNDS = "Fuera de los límites del tablero";
    
    // Errores de bloques de hielo
    public static final String CANNOT_CREATE_ICE = "No se puede crear bloque de hielo en esta posición";
    public static final String CANNOT_DESTROY_ICE = "No se puede destruir bloque de hielo";
    public static final String ICE_BLOCK_EXISTS = "Ya existe un bloque de hielo en esta posición";
    
    // Errores de nivel y configuración
    public static final String INVALID_LEVEL = "Número de nivel inválido";
    public static final String LEVEL_NOT_LOADED = "Error al cargar el nivel";
    public static final String INVALID_FLAVOR = "Sabor de helado inválido";
    
    // Errores de frutas y recolección
    public static final String FRUIT_ALREADY_COLLECTED = "La fruta ya fue recolectada";
    public static final String NO_FRUITS_REMAINING = "No quedan frutas por recolectar";
    
    // Errores de enemigos
    public static final String ENEMY_CREATION_ERROR = "Error al crear enemigo";
    public static final String INVALID_ENEMY_TYPE = "Tipo de enemigo inválido";
    
    // Errores de juego
    public static final String GAME_ALREADY_FINISHED = "El juego ya ha terminado";
    public static final String GAME_NOT_STARTED = "El juego no ha iniciado";
    public static final String TIME_LIMIT_EXCEEDED = "Se acabó el tiempo límite";
    
    // Errores de imágenes y recursos
    public static final String IMAGE_LOAD_ERROR = "Error al cargar imagen";
    public static final String RESOURCE_NOT_FOUND = "Recurso no encontrado";
    
    /**
     * Constructor básico que recibe un mensaje de error
     * @param message El mensaje de error que describe la excepción
     */
    public BadDopoCreamException(String message) {
        super(message);
    }
    
    /**
     * Constructor que recibe un mensaje y la causa original del error
     * @param message El mensaje de error descriptivo
     * @param cause La excepción original que causó este error
     */
    public BadDopoCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}