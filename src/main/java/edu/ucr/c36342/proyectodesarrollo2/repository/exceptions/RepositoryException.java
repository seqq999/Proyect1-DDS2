package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

public class RepositoryException extends Exception {
    public RepositoryException (String message) {super(message);}
    public  RepositoryException (String message, Throwable cause) {super(message, cause);}

}