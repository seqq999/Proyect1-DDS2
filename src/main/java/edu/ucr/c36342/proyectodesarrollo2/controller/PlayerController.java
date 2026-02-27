package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IPlayerRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para operaciones sobre jugadores.
 */
public class PlayerController {

    private IPlayerRepository playerRepo;

    public PlayerController(IPlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    /**
     * Crea un nuevo jugador y lo guarda en el repositorio.
     *
     * @param playerName Nombre del jugador
     * @return true si el jugador fue registrado
     * @throws IOException si ocurre un error de IO
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public boolean registerPlayer(String playerName) throws IOException {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        Player newP = new Player(playerName);
        //el metodo save del repo verifica que el jugador no exista antes de guardarlo
        playerRepo.save(newP);
        return true;
    }

    /**
     * Busca un jugador por su nombre.
     *
     * @param name Nombre del jugador
     * @return El jugador encontrado
     * @throws IOException si ocurre un error de IO
     * @throws PlayerNotFoundException si el jugador no existe
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public Player getPlayerByName(String name) throws IOException, PlayerNotFoundException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        return playerRepo.findByName(name);
    }

    /**
     * Obtiene la lista de todos los jugadores registrados.
     *
     * @return Lista de jugadores
     * @throws IOException si ocurre un error de IO
     */
    public List<Player> getAllPlayers() throws IOException {
        return playerRepo.findAll();
    }

    /**
     * Verifica si un jugador existe por su nombre.
     *
     * @param name Nombre del jugador
     * @return true si el jugador existe
     * @throws IOException si ocurre un error de IO
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public boolean playerExists(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        return playerRepo.exists(name);
    }

    /**
     * Actualiza las estadísticas de un jugador en el repositorio.
     *
     * @param player Jugador a actualizar
     * @return true si la actualización fue exitosa
     * @throws IOException si ocurre un error de IO
     * @throws IllegalArgumentException si el jugador es nulo
     */
    public boolean updatePlayerStats(Player player) throws IOException {
        if (player == null){
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }

        playerRepo.update(player);
        return true;
    }

    /**
     * Elimina un jugador por su nombre.
     *
     * @param name Nombre del jugador a eliminar
     * @return true si el jugador fue eliminado
     * @throws IOException si ocurre un error de IO
     * @throws PlayerNotFoundException si el jugador no existe
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
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
     * Obtiene un jugador por nombre o lo crea si no existe.
     *
     * @param name Nombre del jugador
     * @return El jugador existente o creado
     * @throws IOException si ocurre un error de IO
     * @throws PlayerNotFoundException si el jugador no existe
     * @throws IllegalArgumentException si el nombre es nulo o vacío
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
