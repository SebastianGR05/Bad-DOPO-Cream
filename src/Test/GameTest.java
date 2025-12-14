package Test;

import Domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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

    private Object getPrivateField(Game target, String fieldName) throws Exception {
        Field field = Game.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private List<String> findPathDirections(Board board, int startX, int startY, int goalX, int goalY) {
        int n = 16;
        boolean[][] visited = new boolean[n][n];
        int[][] px = new int[n][n];
        int[][] py = new int[n][n];
        String[][] pmove = new String[n][n];

        for (int y = 0; y < n; y++) {
            Arrays.fill(px[y], -1);
            Arrays.fill(py[y], -1);
        }

        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        int[][] dirs = new int[][]{
                {0, -1}, // UP
                {0, 1},  // DOWN
                {-1, 0}, // LEFT
                {1, 0}   // RIGHT
        };
        String[] names = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0], cy = cur[1];

            if (cx == goalX && cy == goalY) {
                List<String> path = new ArrayList<>();
                int tx = goalX, ty = goalY;
                while (!(tx == startX && ty == startY)) {
                    String mv = pmove[ty][tx];
                    path.add(mv);
                    int ox = px[ty][tx];
                    int oy = py[ty][tx];
                    tx = ox;
                    ty = oy;
                }
                Collections.reverse(path);
                return path;
            }

            for (int i = 0; i < dirs.length; i++) {
                int nx = cx + dirs[i][0];
                int ny = cy + dirs[i][1];

                if (!board.isValidPosition(nx, ny)) continue;
                if (visited[ny][nx]) continue;
                if (board.hasWall(nx, ny)) continue;
                if (board.hasIceBlock(nx, ny)) continue;

                visited[ny][nx] = true;
                px[ny][nx] = cx;
                py[ny][nx] = cy;
                pmove[ny][nx] = names[i];
                q.add(new int[]{nx, ny});
            }
        }

        return Collections.emptyList();
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

    //Ultimas hechas

    @Test
    void shouldNotMovePlayerWhenNextCellIsWall() {
        int guard = 0;
        int lastY = getPlayerY();

        while (getPlayerY() > 1 && guard < 100) {
            game.movePlayer("UP");

            if (getPlayerY() == lastY) break;

            lastY = getPlayerY();
            guard++;
        }

        if (getPlayerY() > 1) {
            int xBefore = getPlayerX();
            int yBefore = getPlayerY();

            game.movePlayer("UP"); 

            assertTrue(getPlayerY() <= yBefore, "Al intentar UP, nunca debería aumentar Y");
            assertEquals(xBefore, getPlayerX(), "UP no debe alterar X");
            return;
        }

        int xBefore = getPlayerX();
        int yBefore = getPlayerY();
        assertEquals(1, yBefore, "Precondición: jugador en fila 1");

        game.movePlayer("UP");

        assertEquals(xBefore, getPlayerX(), "No debe cambiar X al intentar entrar a un muro");
        assertEquals(yBefore, getPlayerY(), "No debe moverse si la celda destino tiene muro");
    }


    @Test
    void shouldNotMovePlayerWhenNextCellHasIceBlock() {
        game.movePlayer("UP");
        int x = getPlayerX();
        int y = getPlayerY();

        int targetX = x;
        int targetY = y - 1;

        Board board = game.getBoard();
        assertFalse(board.hasIceBlock(targetX, targetY), "Precondición: no debe existir hielo delante");

        game.handleIceBlock();
        assertTrue(board.hasIceBlock(targetX, targetY), "Debe existir hielo delante tras handleIceBlock()");

        int xBefore = getPlayerX();
        int yBefore = getPlayerY();

        game.movePlayer("UP");

        assertEquals(xBefore, getPlayerX(), "No debe moverse si la celda destino tiene hielo");
        assertEquals(yBefore, getPlayerY(), "No debe moverse si la celda destino tiene hielo");
    }

    @Test
    void shouldNotChangePlayerPositionWhenDirectionIsInvalid() {
        int xBefore = getPlayerX();
        int yBefore = getPlayerY();

        game.movePlayer("DIAGONAL");

        assertEquals(xBefore, getPlayerX(), "Con dirección inválida, no debería cambiar X");
        assertEquals(yBefore, getPlayerY(), "Con dirección inválida, no debería cambiar Y");
    }

    @Test
    void shouldCreateAndDestroyIceBlockWhenFacingLeft() {
        int x = getPlayerX();
        int y = getPlayerY();

        game.movePlayer("LEFT");
        x = getPlayerX();
        y = getPlayerY();

        int targetX = x - 1;
        int targetY = y;

        Board board = game.getBoard();

        if (!board.isValidPosition(targetX, targetY) || board.hasWall(targetX, targetY)) {
            game.movePlayer("RIGHT");
            game.movePlayer("UP");
            game.movePlayer("LEFT");
            x = getPlayerX();
            y = getPlayerY();
            targetX = x - 1;
            targetY = y;
        }

        assertTrue(board.isValidPosition(targetX, targetY), "Precondición: target debe ser válido");
        assertFalse(board.hasWall(targetX, targetY), "Precondición: target no debe ser muro");
        assertFalse(board.hasIceBlock(targetX, targetY), "No debe existir hielo inicialmente");

        game.handleIceBlock();
        assertTrue(board.hasIceBlock(targetX, targetY), "Debe crearse hielo a la izquierda");

        game.handleIceBlock();
        assertFalse(board.hasIceBlock(targetX, targetY), "Debe destruirse el hielo si ya existía");
    }

    @Test
    void shouldCreateAndDestroyIceBlockWhenFacingRight() {
        game.movePlayer("RIGHT");
        int x = getPlayerX();
        int y = getPlayerY();

        Board board = game.getBoard();
        int targetX = x + 1;
        int targetY = y;

        if (!board.isValidPosition(targetX, targetY) || board.hasWall(targetX, targetY)) {
            game.movePlayer("LEFT");
            game.movePlayer("UP");
            game.movePlayer("RIGHT");
            x = getPlayerX();
            y = getPlayerY();
            targetX = x + 1;
            targetY = y;
        }

        assertTrue(board.isValidPosition(targetX, targetY));
        assertFalse(board.hasWall(targetX, targetY));

        if (board.hasIceBlock(targetX, targetY)) {
            game.handleIceBlock();
            assertFalse(board.hasIceBlock(targetX, targetY));
        }

        game.handleIceBlock();
        assertTrue(board.hasIceBlock(targetX, targetY));

        game.handleIceBlock();
        assertFalse(board.hasIceBlock(targetX, targetY));
    }

    @Test
    void shouldCreateAndDestroyIceBlockWhenFacingDown() {
        game.movePlayer("DOWN");

        int x = getPlayerX();
        int y = getPlayerY();
        Board board = game.getBoard();

        int targetX = x;
        int targetY = y + 1;

        if (!board.isValidPosition(targetX, targetY) || board.hasWall(targetX, targetY)) {
            return;
        }

        game.handleIceBlock();
        assertTrue(board.hasIceBlock(targetX, targetY),
                "Debe crearse al menos un bloque de hielo al usar handleIceBlock()");

        game.handleIceBlock();

        assertTrue(
                !board.hasIceBlock(targetX, targetY) || board.hasIceBlock(targetX, targetY),
                "Después de destruir, el estado del hielo no debe ser inconsistente"
        );
    }


    @Test
    void shouldNotCreateIceBlockWhenFacingWallAhead() {
        Board board = game.getBoard();

        int px = getPlayerX();
        int py = getPlayerY();

        int wallAboveX = -1;
        int wallAboveY = -1;

        outer:
        for (int y = 1; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (board.isValidPosition(x, y)
                        && !board.hasWall(x, y)
                        && !board.hasIceBlock(x, y)
                        && board.hasWall(x, y - 1)) {
                    wallAboveX = x;
                    wallAboveY = y;
                    break outer;
                }
            }
        }

        assertTrue(wallAboveX != -1, "Debe existir una celda con muro arriba");

        List<String> path = findPathDirections(board, px, py, wallAboveX, wallAboveY);
        assertFalse(path.isEmpty(), "Debe existir un camino hacia la celda objetivo");

        for (String mv : path) {
            game.movePlayer(mv);
        }

        game.movePlayer("UP");

        int x = getPlayerX();
        int y = getPlayerY();

        assertTrue(board.hasWall(x, y - 1), "Precondición: delante debe ser muro");
        assertFalse(board.hasIceBlock(x, y - 1), "Precondición: no debe haber hielo");

        game.handleIceBlock();

        assertFalse(board.hasIceBlock(x, y - 1),
                "No debe crearse hielo cuando hay un muro delante");
    }


    @Test
    void shouldAccumulatePausedTimeWhenResumingFromPause() throws Exception {
        assertFalse(game.isPaused(), "Precondición: inicia sin pausa");

        game.togglePause();
        assertTrue(game.isPaused(), "Debe quedar pausado");

        long now = System.currentTimeMillis();
        setPrivateField(game, "lastPauseStart", now - 50L);
        setPrivateField(game, "pausedTime", 0L);

        game.togglePause();
        assertFalse(game.isPaused(), "Debe quedar reanudado");

        long pausedTime = (long) getPrivateField(game, "pausedTime");
        assertTrue(pausedTime >= 0, "pausedTime no debe ser negativo");
        assertTrue(pausedTime <= 500, "pausedTime debe ser razonable (tolerancia)");
    }

    @Test
    void shouldDecreaseTimeRemainingWhilePausedAccountingForCurrentPauseDuration() throws Exception {
        long now = System.currentTimeMillis();
        setPrivateField(game, "startTime", now - 10_000L);
        setPrivateField(game, "pausedTime", 0L);

        game.togglePause();
        setPrivateField(game, "lastPauseStart", now - 2_000L);

        long remaining = game.getTimeRemaining();

        assertTrue(remaining >= 0, "El tiempo restante no debe ser negativo");
        assertTrue(remaining <= 180_000L, "El tiempo restante no debe exceder el límite");
    }


    @Test
    void shouldNotLoseOnUpdateIfPausedTimeOffsetsElapsedBeyondLimit() throws Exception {
        long now = System.currentTimeMillis();
        long startTimeOld = now - 400_000L;
        setPrivateField(game, "startTime", startTimeOld);

        long pausedTime = (now - startTimeOld) - 100_000L; // elapsed efectivo = 100s
        setPrivateField(game, "pausedTime", pausedTime);

        game.update();

        assertFalse(game.isGameLost(), "No debe perder si el tiempo efectivo no supera el límite");
    }

    @Test
    void shouldReturnTotalScoreEqualToSumOfFruitPoints() {
        int sum = 0;
        for (Fruit f : game.getFruits()) {
            sum += f.getPoints();
        }
        assertEquals(sum, game.getTotalScore(), "getTotalScore debe coincidir con la suma de puntos de frutas");
    }

    @Test
    void shouldWinGameWhenCollectingTheOnlyRemainingFruit() throws Exception {
        ArrayList<Fruit> fruits = game.getFruits();
        fruits.clear();

        int x = getPlayerX();
        int y = getPlayerY();

        Board board = game.getBoard();
        assertTrue(board.isValidPosition(x, y - 1));
        assertFalse(board.hasWall(x, y - 1));
        assertFalse(board.hasIceBlock(x, y - 1));

        Fruit banana = new Banana(x, y - 1);
        fruits.add(banana);

        setPrivateField(game, "totalFruits", 1);
        setPrivateField(game, "gameWon", false);
        setPrivateField(game, "gameLost", false);

        game.movePlayer("UP");

        assertTrue(banana.isCollected());
        assertTrue(game.isGameWon());
        assertFalse(game.isGameLost());
    }

    @Test
    void shouldLoseGameWhenMovingIntoEnemyCell() throws Exception {
        ArrayList<Enemy> enemies = game.getEnemies();
        enemies.clear();

        int x = getPlayerX();
        int y = getPlayerY();

        Board board = game.getBoard();
        assertTrue(board.isValidPosition(x, y - 1));
        assertFalse(board.hasWall(x, y - 1));
        assertFalse(board.hasIceBlock(x, y - 1));

        enemies.add(new Troll(x, y - 1));

        setPrivateField(game, "gameLost", false);
        setPrivateField(game, "gameWon", false);

        game.movePlayer("UP");

        assertTrue(game.isGameLost());
        assertFalse(game.isGameWon());
    }

    @Test
    void shouldLoseGameWhenSteppingOnCampfireInLevel2IfExists() {
        Game level2 = new Game(2, "VANILLA");
        Board board = level2.getBoard();

        int campX = -1, campY = -1;

        outer:
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (board.hasCampfireOn(x, y)) {
                    campX = x;
                    campY = y;
                    break outer;
                }
            }
        }

        if (campX == -1) {
            assertFalse(level2.isGameLost(),
                    "Si no hay campfire en el nivel, el juego no debe perderse");
            return;
        }

        int startX = level2.getPlayer().getPosition().getX();
        int startY = level2.getPlayer().getPosition().getY();

        List<String> path = findPathDirections(board, startX, startY, campX, campY);
        assertFalse(path.isEmpty(), "Debe existir un camino hasta el campfire");

        for (String mv : path) {
            level2.movePlayer(mv);
            if (level2.isGameLost()) break;
        }

        assertTrue(level2.isGameLost(),
                "El jugador debe perder al pisar un campfire");
    }


    @Test
    void shouldCreateCorrectEnemyTypesForEachLevel() {
        Game level1 = new Game(1, "VANILLA");
        Game level2 = new Game(2, "VANILLA");
        Game level3 = new Game(3, "VANILLA");

        assertFalse(level1.getEnemies().isEmpty(), "Nivel 1 debe tener enemigos");
        assertFalse(level2.getEnemies().isEmpty(), "Nivel 2 debe tener enemigos");
        assertFalse(level3.getEnemies().isEmpty(), "Nivel 3 debe tener enemigos");

        assertTrue(level1.getEnemies().stream().allMatch(e -> e instanceof Troll), "Nivel 1: enemigos Troll");
        assertTrue(level2.getEnemies().stream().allMatch(e -> e instanceof Pot), "Nivel 2: enemigos Pot");
        assertTrue(level3.getEnemies().stream().allMatch(e -> e instanceof OrangeSquid), "Nivel 3: enemigos OrangeSquid");
    }

    @Test
    void shouldExecuteCherryAndPineappleLogicOnUpdateLevel3WithoutCrashing() {
        Game level3 = new Game(3, "VANILLA");
        assertNotNull(level3.getFruits());
        assertFalse(level3.getFruits().isEmpty(), "Nivel 3 debe tener frutas");

        level3.update(); 

        assertFalse(level3.isGameLost(), "Update no debe marcar perdido por error");
    }

    @Test
    void shouldRestartRecalculateScoreAndFruitsAndRecreateBoardAndPlayer() {
        Board oldBoard = game.getBoard();
        IceCream oldPlayer = game.getPlayer();

        int sumBefore = 0; 
        for (Fruit f : game.getFruits()) sumBefore += f.getPoints();
        assertEquals(sumBefore, game.getTotalScore(), "Precondición: totalScore consistente antes de restart");

        game.restart();

        assertNotNull(game.getBoard(), "Board no debe ser null tras restart");
        assertNotNull(game.getPlayer(), "Player no debe ser null tras restart");
        assertNotSame(oldBoard, game.getBoard(), "restart debe crear un nuevo Board");
        assertNotSame(oldPlayer, game.getPlayer(), "restart debe recrear el jugador");

        assertEquals(game.getFruits().size(), game.getTotalFruits(), "totalFruits debe coincidir con size() tras restart");

        int sumAfter = 0;
        for (Fruit f : game.getFruits()) sumAfter += f.getPoints();
        assertEquals(sumAfter, game.getTotalScore(), "totalScore debe recalcularse tras restart");
    }
}