package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Game;

public interface IGameRepository {
    void save(Game game, String filePath);
    Game load(String filePath);
    boolean exists(String filePath);
}
