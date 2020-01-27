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
public class GenericServerErrorException extends Exception {

    /**
     * Creates a new instance of <code>ServerConnectionErrorException</code>
     * without detail message.
     */
    public GenericServerErrorException() {
    }

    /**
     * Constructs an instance of <code>ServerConnectionErrorException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GenericServerErrorException(String msg) {
        super(msg);
    }
}
