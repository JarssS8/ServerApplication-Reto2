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
public class UserPasswordNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>UserPasswordNotFoundException</code>
     * without detail message.
     */
    public UserPasswordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>UserPasswordNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UserPasswordNotFoundException(String msg) {
        super(msg);
    }
}
