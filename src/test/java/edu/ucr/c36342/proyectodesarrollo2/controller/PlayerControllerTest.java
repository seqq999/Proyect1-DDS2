package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    private PlayerRepositoryFile playerRepo;
    private PlayerController playerController;
    private String filePath = "data/playersTests.xml";

    @BeforeEach
    void setUp() throws IOException {
        playerRepo = new PlayerRepositoryFile(filePath);
        playerController = new PlayerController(playerRepo);
    }
//    @AfterEach
//    void tearDown() throws IOException {
//        // Borra el archivo de jugadores después de cada test para evitar interferencias
//        Files.deleteIfExists(Paths.get(filePath));
//    }

    @Test
    void testRegisterPlayerSuccess() throws IOException, PlayerNotFoundException {
        assertTrue(playerController.registerPlayer("Juan"));
        Player p = playerController.getPlayerByName("Juan");
        assertNotNull(p);
        assertEquals("Juan", p.getName());
    }

    @Test
    void testRegisterPlayerNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> playerController.registerPlayer(null));
    }

    @Test
    void testGetPlayerByNameSuccess() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("Ana");
        Player p = playerController.getPlayerByName("Ana");
        assertNotNull(p);
        assertEquals("Ana", p.getName());
    }

    @Test
    void testGetPlayerByNameEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> playerController.getPlayerByName(""));
    }

    @Test
    void testGetAllPlayersReturnsList() throws IOException {
        playerController.registerPlayer("A");
        playerController.registerPlayer("B");
        List<Player> players = playerController.getAllPlayers();
        assertTrue(players.stream().anyMatch(p -> p.getName().equals("A")));
        assertTrue(players.stream().anyMatch(p -> p.getName().equals("B")));
    }

    @Test
    void testPlayerExistsTrue() throws IOException {
        playerController.registerPlayer("Pedro");
        assertTrue(playerController.playerExists("Pedro"));
    }

    @Test
    void testUpdatePlayerStatsSuccess() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("Luis");
        Player p = playerController.getPlayerByName("Luis");
        p.incrementWins();
        assertTrue(playerController.updatePlayerStats(p));
        Player updated = playerController.getPlayerByName("Luis");
        assertEquals(1, updated.getWins());
    }

    @Test
    void testDeletePlayerSuccess() throws IOException, PlayerNotFoundException {
        playerController.registerPlayer("Carlos");
        assertTrue(playerController.deletePlayer("Carlos"));
        assertThrows(PlayerNotFoundException.class, () -> playerController.getPlayerByName("Carlos"));
    }


}
