/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.exceptions;

/**
 *
 * @author Diego
 */
public class GroupNameAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>GroupNameAlreadyExistException</code>
     * without detail message.
     */
    public GroupNameAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>GroupNameAlreadyExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupNameAlreadyExistException(String msg) {
        super(msg);
    }
}
