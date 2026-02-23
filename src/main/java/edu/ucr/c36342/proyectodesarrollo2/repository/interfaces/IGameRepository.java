package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.GameLoadException;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public interface IGameRepository {
    void save(Game game, String filePath) throws IOException;
    Game load(String filePath) throws IOException, GameLoadException;
    boolean exists(String filePath) throws IOException;
}
