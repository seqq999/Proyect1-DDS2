package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción para cuando no se encuentra un jugador en el repositorio de Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class PlayerNotFoundException extends RepositoryException {
    /**
     * Crea la excepción indicando que no se encontró el jugador.
     * @param message Mensaje descriptivo del error
     */
    public PlayerNotFoundException(String message) {super(message);}
    /**
     * Crea la excepción con mensaje y causa original.
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public PlayerNotFoundException(String message, Throwable cause) {super(message,cause);}
}
