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
public class GroupPasswordNotFound extends Exception {

    /**
     * Creates a new instance of <code>groupPasswordNotFound</code> without
     * detail message.
     */
    public GroupPasswordNotFound() {
    }

    /**
     * Constructs an instance of <code>groupPasswordNotFound</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupPasswordNotFound(String msg) {
        super(msg);
    }
}
