package edu.ucr.c36342.proyectodesarrollo2.repository.implementation;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IPlayerRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación de repositorio de jugadores en memoria para Reverse Dots.
 * Útil para pruebas o sesiones temporales.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class InMemoryPlayerRepository implements IPlayerRepository {
    private Map<String, Player> playerMap;

    public InMemoryPlayerRepository() {

    }
    /**
     * Guarda o actualiza un jugador en memoria.
     *
     * @param player Jugador a guardar
     * @throws IOException nunca lanzada realmente
     * @throws NullPointerException si el jugador es null
     */
    @Override
    public void save(Player player) throws IOException {
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        //guarda o actualiza el jugador en el map
        playerMap.put(player.getName(), player);
    }

    /**
     * Busca un jugador por nombre.
     *
     * @param name Nombre del jugador
     * @return El jugador encontrado
     * @throws IOException nunca lanzada realmente
     * @throws PlayerNotFoundException si el jugador no existe
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    @Override
    public Player findByName(String name) throws IOException, PlayerNotFoundException {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío");
        }
        Player player = playerMap.get(name);
        if (player == null) {
            throw new PlayerNotFoundException("No se encontró el jugador: " + name);
        }
        return player;
    }

    /**
     * Obtiene todos los jugadores en memoria.
     *
     * @return Lista de jugadores
     * @throws IOException nunca lanzada realmente
     */
    @Override
    public List<Player> findAll() throws IOException {
        return new ArrayList<>(playerMap.values());
    }

    /**
     * Actualiza un jugador existente en memoria.
     *
     * @param player Jugador a actualizar
     * @throws IOException nunca lanzada realmente
     * @throws NullPointerException si el jugador es null
     * @throws IllegalArgumentException si el jugador no existe
     */
    @Override
    public void update(Player player) throws IOException{
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        if(!playerMap.containsKey(player.getName())) {
            throw new IllegalArgumentException("El jugador no existe"+ player.getName() + " y no se puede actualizar");
        }
        //actualiza el jugador en el map, si no existe lo agrega
        playerMap.put(player.getName(), player);
    }

    /**
     * Elimina un jugador de memoria.
     *
     * @param player Jugador a eliminar
     * @return true si fue eliminado, false si no existe
     * @throws IOException nunca lanzada realmente
     * @throws NullPointerException si el jugador es null
     */
    @Override
    public boolean delete(Player player) throws IOException {
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        return playerMap.remove(player.getName()) != null;
    }

    /**
     * Verifica si existe un jugador por nombre.
     *
     * @param name Nombre del jugador
     * @return true si existe, false si no
     * @throws IOException nunca lanzada realmente
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    @Override
    public boolean exists(String name) throws IOException{
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser null o vacío");
        }

        return playerMap.containsKey(name);
    }

    /**
     * Verifica si existe un jugador usando un objeto Player.
     *
     * @param player Jugador a buscar
     * @return true si existe, false si no
     * @throws IOException nunca lanzada realmente
     * @throws IllegalArgumentException si el jugador es null
     */
    public boolean exists(Player player) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Player no puede ser null");
        }
        return exists(player.getName());
    }

    /**
     * Elimina todos los jugadores de memoria.
     */
    public void clear() {
        playerMap.clear();
    }

    /**
     * Cuenta la cantidad de jugadores en memoria.
     *
     * @return Número de jugadores
     */
    public int count() {
        return playerMap.size();
    }

}
