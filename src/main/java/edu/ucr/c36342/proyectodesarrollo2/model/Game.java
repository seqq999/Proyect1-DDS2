package edu.ucr.c36342.proyectodesarrollo2.model;

import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.GameStatus;

import java.util.Random;

/**
 * Representa una partida completa del juego Reverse Dots.
 * Contiene el tablero, los jugadores y el estado actual del juego.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class Game {

    private Board board;
    private Player player1;
    private Player player2;
    private Color currentPlayerColor;
    private Color player1Color;
    private Color player2Color;
    private GameStatus gameStatus;

    /**
     * Crea una nueva partida con dos jugadores y un tablero del tamaño especificado.
     * Asigna colores aleatoriamente a los jugadores.
     * El jugador con fichas negras siempre comienza.
     *
     * @param player1 Primer jugador
     * @param player2 Segundo jugador
     * @param boardSize Tamaño del tablero (debe ser par y >= 4)
     * @throws IllegalArgumentException si los jugadores son nulos o tienen el mismo nombre
     */
    public Game(Player player1, Player player2, int boardSize) {
        // Validaciones
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Los jugadores no pueden ser nulos");
        }
        if (player1.getName().equals(player2.getName())) {
            throw new IllegalArgumentException("Los jugadores deben tener nombres diferentes");
        }

        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board(boardSize);

        // Asignar colores aleatoriamente
        assignColorsRandomly();

        // El jugador con fichas negras siempre inicia
        this.currentPlayerColor = Color.BLACK;

        // Estado inicial
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    /**
     * Constructor alternativo para cargar partidas guardadas.
     * Permite especificar todos los parámetros manualmente.
     *
     * @param player1 Primer jugador
     * @param player2 Segundo jugador
     * @param board Tablero con estado específico
     * @param player1Color Color asignado al jugador 1
     * @param player2Color Color asignado al jugador 2
     * @param currentPlayerColor Color del jugador en turno
     * @param gameStatus Estado actual del juego
     */
    public Game(Player player1, Player player2, Board board,
                Color player1Color, Color player2Color,
                Color currentPlayerColor, GameStatus gameStatus) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.currentPlayerColor = currentPlayerColor;
        this.gameStatus = gameStatus;
    }

    /**
     * Asigna colores a los jugadores de forma aleatoria.
     * Un jugador obtiene BLACK y el otro WHITE.
     */
    private void assignColorsRandomly() {
        Random random = new Random();
        if (random.nextBoolean()) {
            this.player1Color = Color.BLACK;
            this.player2Color = Color.WHITE;
        } else {
            this.player1Color = Color.WHITE;
            this.player2Color = Color.BLACK;
        }
    }

    /**
     * Obtiene el tablero del juego.
     *
     * @return El tablero actual
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Obtiene el primer jugador.
     *
     * @return Jugador 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Obtiene el segundo jugador.
     *
     * @return Jugador 2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Obtiene el color del jugador en turno actual.
     *
     * @return Color del turno actual (BLACK o WHITE)
     */
    public Color getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    /**
     * Obtiene el jugador que tiene el turno actual.
     *
     * @return El jugador en turno
     */
    public Player getCurrentPlayer() {
        return currentPlayerColor == player1Color ? player1 : player2;
    }

    /**
     * Obtiene el color asignado al jugador 1.
     *
     * @return Color del jugador 1
     */
    public Color getPlayer1Color() {
        return player1Color;
    }

    /**
     * Obtiene el color asignado al jugador 2.
     *
     * @return Color del jugador 2
     */
    public Color getPlayer2Color() {
        return player2Color;
    }

    /**
     * Obtiene el estado actual del juego.
     *
     * @return Estado del juego (IN_PROGRESS, FINISHED, etc.)
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Establece el estado del juego.
     *
     * @param gameStatus Nuevo estado del juego
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Establece el color del jugador en turno.
     * Útil para cargar partidas guardadas.
     *
     * @param color Color del turno actual
     */
    public void setCurrentPlayerColor(Color color) {
        this.currentPlayerColor = color;
    }

    /**
     * Cambia el turno al siguiente jugador.
     * Alterna entre BLACK y WHITE.
     */
    public void switchTurn() {
        currentPlayerColor = currentPlayerColor.opposite();
    }

    /**
     * Obtiene el jugador oponente al jugador en turno actual.
     *
     * @return El jugador que NO tiene el turno
     */
    public Player getOpponentPlayer() {
        return currentPlayerColor == player1Color ? player2 : player1;
    }

    /**
     * Obtiene el color del oponente del jugador en turno.
     *
     * @return Color del oponente
     */
    public Color getOpponentColor() {
        return currentPlayerColor.opposite();
    }

    /**
     * Verifica si el juego ha terminado.
     *
     * @return true si el estado es FINISHED
     */
    public boolean isFinished() {
        return gameStatus == GameStatus.FINISHED;
    }

    /**
     * Verifica si el juego está en progreso.
     *
     * @return true si el estado es IN_PROGRESS
     */
    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    /**
     * Verifica si el juego no ha empezado.
     *
     * @return true si el estado es NOT_STARTED
     */
    public boolean isNotStarted() {
        return gameStatus == GameStatus.NOT_STARTED;
    }

    /**
     * Reinicia el juego con un nuevo tablero del mismo tamaño.
     * Mantiene los mismos jugadores pero reasigna colores aleatoriamente.
     */
    public void reset() {
        int currentSize = board.getSize();
        this.board = new Board(currentSize);
        assignColorsRandomly();
        this.currentPlayerColor = Color.BLACK;
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return String.format("Game{player1=%s(%s), player2=%s(%s), currentTurn=%s, status=%s}",
                player1.getName(), player1Color,
                player2.getName(), player2Color,
                getCurrentPlayer().getName(),
                gameStatus);
    }
}