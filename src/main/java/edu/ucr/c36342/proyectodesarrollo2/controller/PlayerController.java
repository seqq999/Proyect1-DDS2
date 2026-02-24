package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.util.List;

public class PlayerController {

    private PlayerRepositoryFile playerRepo;

    public PlayerController(PlayerRepositoryFile playerRepo) {
        this.playerRepo = playerRepo;
    }

    public boolean registerPlayer(String playerName) throws IOException {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        Player newP = new Player(playerName);
        //el metodo save del repo verifica que el jugador no exista antes de guardarlo
        playerRepo.save(newP);
        return true;
    }

    public Player getPlayerByName(String name) throws IOException, PlayerNotFoundException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        return playerRepo.findByName(name);
    }

    public List<Player> getAllPlayers() throws IOException {
        return playerRepo.findAll();
    }

    public boolean playerExists(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        return playerRepo.exists(name);
    }

    public boolean updatePlayerStats(Player player) throws IOException {
        if (player == null){
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }

        playerRepo.update(player);
        return true;
    }

    public boolean deletePlayer(String name) throws IOException, PlayerNotFoundException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        Player player = playerRepo.findByName(name);
        if(player == null){
            throw new PlayerNotFoundException("No se encontró el jugador: " + name);
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
    public Player getOrCreatePlayer(String name) throws IOException, PlayerNotFoundException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }

        boolean exists = playerRepo.exists(name);

        Player existing = null;
        if (exists == true) {
            existing = playerRepo.findByName(name);
            return existing;
        }


        if (existing != null) {
            return existing;
        } else {
            Player newP = new Player(name);
            playerRepo.save(newP);
            return newP;
        }
    }
}
