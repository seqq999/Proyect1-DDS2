package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;

import java.io.IOException;
import java.util.List;

public class PlayerController {

    private PlayerRepositoryFile playerRepo;

    public PlayerController(PlayerRepositoryFile playerRepo) {
        this.playerRepo = playerRepo;
    }

    public Player registerPlayer(String playerName) throws IOException {
        Player newP = new Player(playerName);
        playerRepo.save(newP);
        return newP;
    }

    public Player getPlayerByName(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Player name is null or empty");
        }

        return playerRepo.findByName(name);
    }

    public List<Player> getAllPlayers() throws IOException {
        return playerRepo.findAll();
    }

    public boolean playerExists(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Player name is null or empty");
        }
        return playerRepo.exists(name);
    }

    public void updatePlayerStats(Player player) throws IOException {
        if (player == null){
            throw new IllegalArgumentException("Player is null");
        }

        playerRepo.update(player);
    }
}
