package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

public class GameLoadException extends RepositoryException {
    public GameLoadException(String message) {
        super(message);
    }
    public GameLoadException(String message, Throwable cause) {super(message,cause);}
}
