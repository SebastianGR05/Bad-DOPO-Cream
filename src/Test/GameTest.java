package Test;
import Domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para Game
 */
public class GameTest {

    private Game game;

    @BeforeEach  
    void setUp() throws BadDopoCreamException {
        game = new Game(1, "VANILLA");
    }

    private int getPlayerX() {
        return game.getPlayer().getPosition().getX();
    }

    private int getPlayerY() {
        return game.getPlayer().getPosition().getY();
    }

    // Método para modificar campos privados usando reflexión
    private void setPrivateField(Game target, String fieldName, Object value) throws Exception {
        Field field = Game.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void shouldInitializeBoardPlayerEnemiesAndFruitsOnCreation() {
        assertNotNull(game.getBoard(), "El tablero no debería ser null");
        assertNotNull(game.getPlayer(), "El jugador no debería ser null");
        assertNotNull(game.getEnemies(), "La lista de enemigos no debería ser null");
        assertNotNull(game.getFruits(), "La lista de frutas no debería ser null");
        assertEquals(game.getFruits().size(), game.getTotalFruits(),
                "totalFruits debe coincidir con el tamaño de la lista de frutas");
        assertFalse(game.isGameWon(), "Al iniciar el juego no debe estar ganado");
        assertFalse(game.isGameLost(), "Al iniciar el juego no debe estar perdido");
    }

    @Test
    void shouldSetCurrentLevelFromConstructorParameter() {
        assertEquals(1, game.getCurrentLevel(), "El nivel actual debe coincidir con el parámetro del constructor");
    }

    @Test
    void shouldMovePlayerWhenDirectionIsValidAndNotBlocked() {
        int initialX = getPlayerX();
        int initialY = getPlayerY();

        game.movePlayer("UP");

        assertEquals(initialX, getPlayerX(), "El jugador no debería cambiar de columna al moverse hacia arriba");
        assertEquals(initialY - 1, getPlayerY(), "El jugador debería haber subido una fila");
    }

    @Test
    void shouldNotMovePlayerWhenPaused() {
        int initialX = getPlayerX();
        int initialY = getPlayerY();

        game.togglePause();
        game.movePlayer("UP");

        assertEquals(initialX, getPlayerX(), "El jugador no debe moverse si el juego está en pausa");
        assertEquals(initialY, getPlayerY(), "El jugador no debe moverse si el juego está en pausa");
    }

    @Test
    void shouldNotMovePlayerWhenGameIsWonOrLost() throws Exception {
        int initialX = getPlayerX();
        int initialY = getPlayerY();

        setPrivateField(game, "gameWon", true);
        game.movePlayer("UP");
        assertEquals(initialX, getPlayerX(), "El jugador no debe moverse si el juego está ganado");
        assertEquals(initialY, getPlayerY(), "El jugador no debe moverse si el juego está ganado");

        setPrivateField(game, "gameWon", false);
        setPrivateField(game, "gameLost", true);

        game.movePlayer("UP");
        assertEquals(initialX, getPlayerX(), "El jugador no debe moverse si el juego está perdido");
        assertEquals(initialY, getPlayerY(), "El jugador no debe moverse si el juego está perdido");
    }

    @Test
    void shouldCreateIceBlockWhenNoIceBlockAhead() {
        game.movePlayer("UP");
        int x = getPlayerX();
        int y = getPlayerY();

        int targetX = x;
        int targetY = y - 1; 

        Board board = game.getBoard();
        assertFalse(board.hasIceBlock(targetX, targetY), "No debe haber bloque de hielo inicialmente en la casilla objetivo");

        game.handleIceBlock();

        assertTrue(board.hasIceBlock(targetX, targetY),
                "Debe crearse un bloque de hielo si no había y la posición es válida y sin muro");
    }

    @Test
    void shouldDestroyIceBlockWhenIceBlockAhead() {
        game.movePlayer("UP");
        int x = getPlayerX();
        int y = getPlayerY();
        int targetX = x;
        int targetY = y - 1;

        Board board = game.getBoard();
        game.handleIceBlock();
        assertTrue(board.hasIceBlock(targetX, targetY), "Debe existir un bloque de hielo después de la primera llamada");

        game.handleIceBlock();
        assertFalse(board.hasIceBlock(targetX, targetY),
                "El bloque de hielo debería destruirse si ya existía delante del jugador");
    }

    @Test
    void shouldNotHandleIceBlockWhenGamePausedOrFinished() throws Exception {
        game.movePlayer("UP");
        int x = getPlayerX();
        int y = getPlayerY();
        int targetX = x;
        int targetY = y - 1;

        Board board = game.getBoard();
        assertFalse(board.hasIceBlock(targetX, targetY));

        game.togglePause();
        game.handleIceBlock();
        assertFalse(board.hasIceBlock(targetX, targetY),
                "No debe crearse bloque de hielo si el juego está en pausa");

        game.togglePause(); 
        setPrivateField(game, "gameLost", true);
        game.handleIceBlock();
        assertFalse(board.hasIceBlock(targetX, targetY),
                "No debe crearse bloque de hielo si el juego está perdido");
    }

    @Test
    void shouldSetGameLostWhenTimeLimitExceededOnUpdate() throws Exception {
        long veryOldStartTime = System.currentTimeMillis() - 200000L;
        setPrivateField(game, "startTime", veryOldStartTime);

        game.update();

        assertTrue(game.isGameLost(), "El juego debe marcarse como perdido si el tiempo límite se supera");
    }

    @Test
    void shouldNotUpdateEnemiesWhenPaused() {
        List<Integer> xsBefore = new ArrayList<>();
        List<Integer> ysBefore = new ArrayList<>();
        for (Enemy enemy : game.getEnemies()) {
            xsBefore.add(enemy.getPosition().getX());
            ysBefore.add(enemy.getPosition().getY());
        }

        game.togglePause();
        game.update();

        List<Enemy> enemiesAfter = game.getEnemies();
        for (int i = 0; i < enemiesAfter.size(); i++) {
            assertEquals(xsBefore.get(i).intValue(), enemiesAfter.get(i).getPosition().getX(),
                    "El enemigo no debe moverse si el juego está en pausa");
            assertEquals(ysBefore.get(i).intValue(), enemiesAfter.get(i).getPosition().getY(),
                    "El enemigo no debe moverse si el juego está en pausa");
        }
    }

    @Test
    void shouldNotLoseGameOnUpdateBeforeTimeLimit() {
        for (int i = 0; i < 5; i++) {
            game.update();
        }
        assertFalse(game.isGameLost(), "No debe marcar el juego como perdido si no ha pasado el tiempo límite");
    }

    @Test
    void shouldTogglePauseState() {
        assertFalse(game.isPaused(), "Al inicio el juego no debe estar en pausa");
        game.togglePause();
        assertTrue(game.isPaused(), "Después de togglePause debe estar en pausa");
        game.togglePause();
        assertFalse(game.isPaused(), "Después de otro togglePause no debe estar en pausa");
    }

    @Test
    void shouldNotChangeGameResultFlagsWhenTogglingPause() throws Exception {
        setPrivateField(game, "gameWon", true);
        boolean wonBefore = game.isGameWon();
        boolean lostBefore = game.isGameLost();

        game.togglePause();

        assertEquals(wonBefore, game.isGameWon(), "togglePause no debe cambiar el estado de juego ganado");
        assertEquals(lostBefore, game.isGameLost(), "togglePause no debe cambiar el estado de juego perdido");
    }

    @Test
    void shouldReturnNonNullBoard() {
        assertNotNull(game.getBoard(), "getBoard nunca debe devolver null");
    }

    @Test
    void shouldNotReturnDifferentBoardInstancesOnMultipleCalls() {
        Board board1 = game.getBoard();
        Board board2 = game.getBoard();
        assertSame(board1, board2, "getBoard debe devolver siempre la misma instancia de Board");
    }

    @Test
    void shouldReturnNonNullPlayer() {
        assertNotNull(game.getPlayer(), "getPlayer nunca debe devolver null");
    }

    @Test
    void shouldNotReturnDifferentPlayerInstancesOnMultipleCalls() {
        IceCream p1 = game.getPlayer();
        IceCream p2 = game.getPlayer();
        assertSame(p1, p2, "getPlayer debe devolver siempre la misma instancia de IceCream");
    }

    @Test
    void shouldReturnNonNullEnemiesList() {
        assertNotNull(game.getEnemies(), "getEnemies nunca debe devolver null");
    }

    @Test
    void shouldNotReturnDifferentEnemiesListInstancesOnMultipleCalls() {
        ArrayList<Enemy> enemies1 = game.getEnemies();
        ArrayList<Enemy> enemies2 = game.getEnemies();
        assertSame(enemies1, enemies2, "getEnemies debe devolver la misma lista interna de enemigos");
    }

    @Test
    void shouldReturnNonNullFruitsList() {
        assertNotNull(game.getFruits(), "getFruits nunca debe devolver null");
    }

    @Test
    void shouldNotReturnDifferentFruitsListInstancesOnMultipleCalls() {
        ArrayList<Fruit> fruits1 = game.getFruits();
        ArrayList<Fruit> fruits2 = game.getFruits();
        assertSame(fruits1, fruits2, "getFruits debe devolver la misma lista interna de frutas");
    }

    @Test
    void shouldNotBeWonOrLostAtStart() {
        assertFalse(game.isGameWon(), "Al inicio el juego no debe estar ganado");
        assertFalse(game.isGameLost(), "Al inicio el juego no debe estar perdido");
    }

    @Test
    void shouldReflectInternalGameWonFlagOnIsGameWon() throws Exception {
        setPrivateField(game, "gameWon", true);
        assertTrue(game.isGameWon(), "isGameWon debe reflejar el valor interno de gameWon");
        setPrivateField(game, "gameWon", false);
        assertFalse(game.isGameWon(), "isGameWon debe reflejar el valor interno de gameWon=false");
    }

    @Test
    void shouldReflectInternalGameLostFlagOnIsGameLost() throws Exception {
        setPrivateField(game, "gameLost", true);
        assertTrue(game.isGameLost(), "isGameLost debe reflejar el valor interno de gameLost");
        setPrivateField(game, "gameLost", false);
        assertFalse(game.isGameLost(), "isGameLost debe reflejar el valor interno de gameLost=false");
    }

    @Test
    void shouldReturnNonNegativeTimeRemaining() {
        long remaining = game.getTimeRemaining();
        assertTrue(remaining >= 0, "El tiempo restante nunca debe ser negativo");
    }

    @Test
    void shouldNotReturnValueGreaterThanTimeLimit() {
        long remaining = game.getTimeRemaining();
        long timeLimit = 180000L; // Debe coincidir con TIME_LIMIT de Game
        assertTrue(remaining <= timeLimit,
                "El tiempo restante no debe ser mayor que el tiempo límite configurado");
    }

    @Test
    void shouldReturnTotalFruitsMatchingFruitsListSize() {
        assertEquals(game.getFruits().size(), game.getTotalFruits(),
                "getTotalFruits debe coincidir con el tamaño de la lista de frutas");
    }

    @Test
    void shouldNotReturnNegativeTotalFruits() {
        assertTrue(game.getTotalFruits() >= 0, "El número total de frutas no debe ser negativo");
    }

    @Test
    void shouldReturnSameLevelAsConstructor() {
        assertEquals(1, game.getCurrentLevel(),
                "getCurrentLevel debe devolver el nivel pasado al constructor");
    }

    @Test
    void shouldNotChangeCurrentLevelAfterRestart() {
        int levelBefore = game.getCurrentLevel();
        game.restart();
        int levelAfter = game.getCurrentLevel();
        assertEquals(levelBefore, levelAfter,
                "restart no debe cambiar el nivel actual del juego");
    }

    @Test
    void shouldRestartLevelAndResetGameState() {
        int initialLevel = game.getCurrentLevel();
        int initialX = getPlayerX();
        int initialY = getPlayerY();
        int initialTotalFruits = game.getTotalFruits();

        game.movePlayer("UP");
        game.togglePause();

        game.restart();

        assertEquals(initialLevel, game.getCurrentLevel(), "El nivel no debe cambiar tras restart");

        assertFalse(game.isGameWon(), "Después de restart el juego no debe estar ganado");
        assertFalse(game.isGameLost(), "Después de restart el juego no debe estar perdido");

        assertEquals(initialX, getPlayerX(), "El jugador debe volver a su posición inicial tras restart");
        assertEquals(initialY, getPlayerY(), "El jugador debe volver a su posición inicial tras restart");

        assertEquals(initialTotalFruits, game.getTotalFruits(),
                "El número total de frutas debe coincidir con el inicial tras restart");
        assertEquals(game.getFruits().size(), game.getTotalFruits(),
                "La lista de frutas debe coincidir con totalFruits tras restart");
    }
}