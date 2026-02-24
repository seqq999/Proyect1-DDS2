package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.controller.enums.GameResult;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.model.Board;
import edu.ucr.c36342.proyectodesarrollo2.model.Position;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {
    private PlayerRepositoryFile playerRepo;
    private GameRepository gameRepo;
    private PlayerController playerController;
    private GameController gameController;
    private Game game;
    private Board board;

    @BeforeEach
    void setUp() {
        playerRepo = mock(PlayerRepositoryFile.class);
        gameRepo = mock(GameRepository.class);
        playerController = mock(PlayerController.class);
        gameController = new GameController(playerRepo, gameRepo, playerController);
        game = mock(Game.class);
        board = mock(Board.class);
    }

    @Test
    void testStartNewGameCreatesGame() throws IOException, PlayerNotFoundException {
        when(playerRepo.exists(anyString())).thenReturn(false);
        doNothing().when(playerRepo).save(any(Player.class));
        when(playerRepo.findByName(anyString())).thenReturn(new Player("A"), new Player("B"));
        gameController.startNewGame("A", "B", 8);
        // No exception means game created
    }

    @Test
    void testStartNewGameThrowsOnNullNames() {
        assertThrows(IllegalArgumentException.class, () -> gameController.startNewGame(null, null, 8));
    }

    @Test
    void testMakeMoveReturnsSuccess() throws IOException {
        gameController = spy(gameController);
        gameController.setGame(game);
        when(game.getBoard()).thenReturn(board);
        when(board.isValidMove(anyInt(), anyInt(), any(Color.class))).thenReturn(true);
        doNothing().when(gameController).makeMove(anyInt(), anyInt());
        when(gameController.isGameOver()).thenReturn(false);
        when(game.getCurrentPlayerColor()).thenReturn(Color.BLACK);
        assertEquals(GameResult.SUCCESS, gameController.makeMove(1,1));
    }

    @Test
    void testMakeMoveThrowsIfNoGame() {
        gameController = spy(gameController);
        gameController.setGame(null);
        assertThrows(IllegalStateException.class, () -> gameController.makeMove(1,1));
    }

    @Test
    void testGetValidMovesReturnsList() {
        gameController = spy(gameController);
        gameController.setGame(game);
        List<Position> moves = Arrays.asList(new Position(0,0), new Position(1,1));
        when(gameController.getValidMoves()).thenReturn(moves);
        when(gameController.getValidMoves()).thenCallRealMethod();
        assertEquals(moves, gameController.getValidMoves());
    }

    @Test
    void testIsGameStartedReturnsTrue() {
        gameController = spy(gameController);
        gameController.setGame(game);
        assertTrue(gameController.isGameStarted());
    }

    @Test
    void testIsGameOverReturnsFalse() {
        gameController = spy(gameController);
        gameController.setGame(game);
        when(gameController.isGameOver()).thenReturn(false);
        when(gameController.isGameOver()).thenCallRealMethod();
        assertFalse(gameController.isGameOver());
    }
}
