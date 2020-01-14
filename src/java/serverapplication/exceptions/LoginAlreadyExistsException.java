/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.exceptions;

/**
 *
 * @author aimar
 */
public class LoginAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of <code>LoginAlreadyExistsException</code>
     * without detail message.
     */
    public LoginAlreadyExistsException() {
    }

    /**
     * Constructs an instance of <code>LoginAlreadyExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginAlreadyExistsException(String msg) {
        super(msg);
    }
}
