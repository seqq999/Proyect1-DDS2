package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;

import java.util.List;

public interface IPlayerRepository {
    void save(Player player);
    Player findByName(String name);
    List<Player> findAll();
    void update(Player player);
    void delete(Player player);
    boolean exists(String name);
}
