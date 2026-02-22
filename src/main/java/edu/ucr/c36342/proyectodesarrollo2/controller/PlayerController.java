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

    public boolean registerPlayer(String playerName) throws IOException {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        Player newP = new Player(playerName);
        //el metodo save del repo verifica que el jugador no exista antes de guardarlo
        playerRepo.save(newP);
        return true     ;
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

    public boolean updatePlayerStats(Player player) throws IOException {
        if (player == null){
            throw new IllegalArgumentException("Player is null");
        }

        playerRepo.update(player);
        return true;
    }

    public boolean deletePlayer(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Player name is null or empty");
        }
        Player player = playerRepo.findByName(name);

        if(player == null){
            return false;
        }

        return playerRepo.delete(player);
    }

    /**
     *
     * Obtiene un jugador por su nombre o lo crea si no existe.
     * @param name
     * @return
     * @throws IOException
     */
    public Player getOrCreatePlayer(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Player name is null or empty");
        }

        Player existing = playerRepo.findByName(name);

        if(existing != null){
            return existing;
        }

        Player newPlayer = new Player(name);
        playerRepo.save(newPlayer);
        return newPlayer;
    }
}
