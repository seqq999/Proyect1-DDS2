package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

public class PlayerNotFoundException extends RepositoryException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
    public PlayerNotFoundException(String message, Throwable cause) {super(message,cause);}
}
