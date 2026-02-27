package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción para errores al cargar una partida en Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */

/**
 * Excepción para errores al cargar una partida.
 * Ejemplo: throw new GameLoadException("No se pudo cargar la partida guardada");
 */
public class GameLoadException extends RepositoryException {
    /**
     * Crea la excepción para error de carga de partida.
     * @param message Mensaje descriptivo del error
     */
    public GameLoadException(String message) {
        super(message);
    }
    /**
     * Crea la excepción con mensaje y causa original.
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public GameLoadException(String message, Throwable cause) {super(message,cause);}
}
