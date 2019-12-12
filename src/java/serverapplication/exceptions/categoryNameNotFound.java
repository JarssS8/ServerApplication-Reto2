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
public class categoryNameNotFound extends Exception {

    /**
     * Creates a new instance of <code>categoryNameNotFound</code> without
     * detail message.
     */
    public categoryNameNotFound() {
    }

    /**
     * Constructs an instance of <code>categoryNameNotFound</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public categoryNameNotFound(String msg) {
        super(msg);
    }
}
