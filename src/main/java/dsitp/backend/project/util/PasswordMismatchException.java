/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package dsitp.backend.project.util;

/**
 *
 * @author franciscokuchen
 */
public class PasswordMismatchException extends RuntimeException {

    /**
     * Creates a new instance of <code>PasswordMismatchException</code> without
     * detail message.
     */
    public PasswordMismatchException() {
    }

    /**
     * Constructs an instance of <code>PasswordMismatchException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordMismatchException(String msg) {
        super("Error");
    }
}
