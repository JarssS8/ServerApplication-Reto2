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
public class ServerConnectionErrorException extends Exception {

    /**
     * Creates a new instance of <code>ServerConnectionErrorException</code>
     * without detail message.
     */
    public ServerConnectionErrorException() {
    }

    /**
     * Constructs an instance of <code>ServerConnectionErrorException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ServerConnectionErrorException(String msg) {
        super(msg);
    }
}
