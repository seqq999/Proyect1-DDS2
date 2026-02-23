package edu.ucr.c36342.proyectodesarrollo2.repository.exceptions;

/**
 * Excepción base para errores de repositorio.
 * Se utiliza para representar cualquier error relacionado con operaciones de almacenamiento o recuperación de datos.
 * Ejemplo: throw new RepositoryException("No se pudo guardar el archivo");
 */
public class RepositoryException extends Exception {
    public RepositoryException (String message) {super(message);}
    public  RepositoryException (String message, Throwable cause) {super(message, cause);}

}