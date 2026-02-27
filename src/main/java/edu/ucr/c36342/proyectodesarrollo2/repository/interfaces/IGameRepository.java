package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.GameLoadException;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz para repositorios de partidas guardadas de Reverse Dots.
 * Define operaciones para guardar, cargar y verificar partidas.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public interface IGameRepository {
    /**
     * Guarda una partida en el repositorio.
     * @param game Partida a guardar
     * @param filePath Ruta del archivo donde guardar
     * @throws IOException si ocurre un error de IO
     */
    void save(Game game, String filePath) throws IOException;

    /**
     * Carga una partida desde el repositorio.
     * @param filePath Ruta del archivo a cargar
     * @return La partida cargada
     * @throws IOException si ocurre un error de IO
     * @throws GameLoadException si el archivo no existe o está corrupto
     */
    Game load(String filePath) throws IOException, GameLoadException;

    /**
     * Verifica si existe una partida guardada en la ruta dada.
     * @param filePath Ruta del archivo
     * @return true si existe, false si no
     * @throws IOException si ocurre un error de IO
     */
    boolean exists(String filePath) throws IOException;
}
