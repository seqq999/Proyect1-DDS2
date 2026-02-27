package edu.ucr.c36342.proyectodesarrollo2.repository.interfaces;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz para repositorios de jugadores de Reverse Dots.
 * Define operaciones CRUD y consulta de existencia.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public interface IPlayerRepository {
    /**
     * Guarda un jugador en el repositorio.
     * @param player Jugador a guardar
     * @throws IOException si ocurre un error de IO
     */
    void save(Player player) throws IOException;

    /**
     * Busca un jugador por nombre.
     * @param name Nombre del jugador
     * @return El jugador encontrado
     * @throws IOException si ocurre un error de IO
     * @throws PlayerNotFoundException si el jugador no existe
     */
    Player findByName(String name) throws IOException, PlayerNotFoundException;

    /**
     * Obtiene todos los jugadores del repositorio.
     * @return Lista de jugadores
     * @throws IOException si ocurre un error de IO
     */
    List<Player> findAll() throws IOException;

    /**
     * Actualiza los datos de un jugador.
     * @param player Jugador a actualizar
     * @throws IOException si ocurre un error de IO
     */
    void update(Player player) throws IOException;

    /**
     * Elimina un jugador del repositorio.
     * @param player Jugador a eliminar
     * @return true si fue eliminado, false si no existe
     * @throws IOException si ocurre un error de IO
     */
    boolean delete(Player player) throws IOException;

    /**
     * Verifica si existe un jugador por nombre.
     * @param name Nombre del jugador
     * @return true si existe, false si no
     * @throws IOException si ocurre un error de IO
     */
    boolean exists(String name) throws IOException;
}
