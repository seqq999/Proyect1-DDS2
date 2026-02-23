package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción para cuando no se encuentra un jugador en el repositorio.
 * Ejemplo: throw new PlayerNotFoundException("No se encontró el jugador: Juan");
 */
public class PlayerNotFoundException extends RepositoryException {
    public PlayerNotFoundException(String message) {super(message);}
    public PlayerNotFoundException(String message, Throwable cause) {super(message,cause);}
}
