package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IPlayerRepository {
    void save(Player player) throws IOException;
    Player findByName(String name) throws IOException, PlayerNotFoundException;
    List<Player> findAll() throws IOException;
    void update(Player player) throws IOException;
    boolean delete(Player player) throws IOException;
    boolean exists(String name) throws IOException;
}
