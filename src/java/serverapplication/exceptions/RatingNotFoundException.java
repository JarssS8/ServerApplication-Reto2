/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.exceptions;

/**
 *
 * @author Gaizka Andres
 */
public class RatingNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>reviewNotFound</code> without detail
     * message.
     */
    public RatingNotFoundException() {
    }

    /**
     * Constructs an instance of <code>reviewNotFound</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public RatingNotFoundException(String msg) {
        super(msg);
    }
}
