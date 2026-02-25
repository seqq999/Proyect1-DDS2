package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.controller.enums.GameResult;
import edu.ucr.c36342.proyectodesarrollo2.model.Position;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private PlayerRepositoryFile playerRepo;
    private GameRepository gameRepo;
    private PlayerController playerController;
    private GameController gameController;
    private String playersFilePath = "data/playersTests.xml";

    @BeforeEach
    void setUp() throws IOException {
        playerRepo = new PlayerRepositoryFile(playersFilePath);
        gameRepo = new GameRepository();
        playerController = new PlayerController(playerRepo);
        gameController = new GameController(playerRepo, gameRepo, playerController);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Borra el archivo de jugadores después de cada test para evitar interferencias
        Files.deleteIfExists(Paths.get(playersFilePath));
    }

    @Test
    void testStartNewGameCreatesGame() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");

        gameController.startNewGame("A", "B", 8);

        assertTrue(gameController.isGameStarted());
    }

    @Test
    void testStartNewGameThrowsOnNullNames() {
        assertThrows(IllegalArgumentException.class, () -> gameController.startNewGame(null, null, 8));
    }

    @Test
    void testMakeMoveReturnsSuccess() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");
        gameController.startNewGame("A", "B", 8);
        // Busca una jugada válida para el jugador actual
        List<Position> validMoves = gameController.getValidMoves();
        assertFalse(validMoves.isEmpty());
        Position move = validMoves.get(0);
        GameResult result = gameController.makeMove(move.getRow(), move.getCol());
        assertEquals(GameResult.SUCCESS, result);
    }

    @Test
    void testMakeMoveThrowsIfNoGame() {
        gameController = new GameController(playerRepo, gameRepo, playerController);
        assertThrows(IllegalStateException.class, () -> gameController.makeMove(1,1));
    }

    @Test
    void testGetValidMovesReturnsList() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");
        gameController.startNewGame("A", "B", 8);
        List<Position> moves = gameController.getValidMoves();
        assertNotNull(moves);
        assertFalse(moves.isEmpty());
    }

    @Test
    void testIsGameStartedReturnsTrue() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");
        gameController.startNewGame("A", "B", 8);
        assertTrue(gameController.isGameStarted());
    }

    @Test
    void testIsGameOverReturnsFalse() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");
        gameController.startNewGame("A", "B", 8);
        assertFalse(gameController.isGameOver());
    }
}
