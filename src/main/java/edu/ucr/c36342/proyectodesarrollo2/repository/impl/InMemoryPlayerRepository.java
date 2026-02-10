package edu.ucr.c36342.proyectodesarrollo2.repository.impl;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IPlayerRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryPlayerRepository implements IPlayerRepository {
    private Map<String, Player> playerMap;

    public InMemoryPlayerRepository() {

    }
    @Override
    public void save(Player player) throws IOException {
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        //guarda o actualiza el jugador en el map
        playerMap.put(player.getName(), player);
    }

    @Override
    public Player findByName(String name) throws IOException {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser null o vacío");
        }

        //devuelve null si el jugador no existe
        return playerMap.get(name);
    }

    @Override
    public List<Player> findAll() throws IOException {
        return new ArrayList<>(playerMap.values());
    }

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

    @Override
    public boolean delete(Player player) throws IOException {
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        return playerMap.remove(player.getName()) != null;
    }

    @Override
    public boolean exists(String name) throws IOException{
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser null o vacío");
        }

        return playerMap.containsKey(name);
    }
    public boolean exists(Player player) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Player no puede ser null");
        }
        return exists(player.getName());
    }
    
    public void clear() {
        playerMap.clear();
    }

    public int count() {
        return playerMap.size();
    }

}
