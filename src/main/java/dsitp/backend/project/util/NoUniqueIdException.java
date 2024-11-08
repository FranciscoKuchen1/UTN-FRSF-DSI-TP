/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package dsitp.backend.project.util;

/**
 *
 * @author franciscokuchen
 */
public class NoUniqueIdException extends RuntimeException {

    /**
     * Creates a new instance of <code>NoUniqueIdException</code> without detail
     * message.
     */
    public NoUniqueIdException() {
    }

    /**
     * Constructs an instance of <code>NoUniqueIdException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoUniqueIdException(String msg) {
        super(msg);
    }
}
