package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción para errores al cargar una partida.
 * Ejemplo: throw new GameLoadException("No se pudo cargar la partida guardada");
 */
public class GameLoadException extends RepositoryException {
    public GameLoadException(String message) {
        super(message);
    }
    public GameLoadException(String message, Throwable cause) {super(message,cause);}
}
