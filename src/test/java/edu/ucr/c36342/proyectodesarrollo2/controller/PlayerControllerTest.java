package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class
PlayerControllerTest {
    private PlayerRepositoryFile playerRepo;
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        playerRepo = mock(PlayerRepositoryFile.class);
        playerController = new PlayerController(playerRepo);
    }

    @Test
    void testRegisterPlayerSuccess() throws IOException {
        doNothing().when(playerRepo).save(any(Player.class));
        assertTrue(playerController.registerPlayer("Juan"));
    }

    @Test
    void testRegisterPlayerNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> playerController.registerPlayer(null));
    }

    @Test
    void testGetPlayerByNameSuccess() throws IOException, PlayerNotFoundException {
        Player p = new Player("Ana");
        when(playerRepo.findByName("Ana")).thenReturn(p);
        assertEquals(p, playerController.getPlayerByName("Ana"));
    }

    @Test
    void testGetPlayerByNameEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> playerController.getPlayerByName(""));
    }

    @Test
    void testGetAllPlayersReturnsList() throws IOException {
        List<Player> players = Arrays.asList(new Player("A"), new Player("B"));
        when(playerRepo.findAll()).thenReturn(players);
        assertEquals(players, playerController.getAllPlayers());
    }

    @Test
    void testPlayerExistsTrue() throws IOException {
        when(playerRepo.exists("Pedro")).thenReturn(true);
        assertTrue(playerController.playerExists("Pedro"));
    }

    @Test
    void testUpdatePlayerStatsSuccess() throws IOException {
        Player p = new Player("Luis");
        doNothing().when(playerRepo).update(p);
        assertTrue(playerController.updatePlayerStats(p));
    }

    @Test
    void testDeletePlayerSuccess() throws IOException, PlayerNotFoundException {
        Player p = new Player("Carlos");
        when(playerRepo.findByName("Carlos")).thenReturn(p);
        when(playerRepo.delete(p)).thenReturn(true);
        assertTrue(playerController.deletePlayer("Carlos"));
    }
}

