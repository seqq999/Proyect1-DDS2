package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción base para errores de repositorio en Reverse Dots.
 * Se utiliza para representar cualquier error relacionado con operaciones de almacenamiento o recuperación de datos.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class RepositoryException extends Exception {
    /**
     * Crea una excepción de repositorio con un mensaje.
     * @param message Mensaje descriptivo del error
     */
    public RepositoryException (String message) {super(message);}
    /**
     * Crea una excepción de repositorio con mensaje y causa.
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public  RepositoryException (String message, Throwable cause) {super(message, cause);}

}