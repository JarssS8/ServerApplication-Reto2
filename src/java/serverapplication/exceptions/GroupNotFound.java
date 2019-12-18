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
public class GroupNotFound extends Exception {

    /**
     * Creates a new instance of <code>groupNotFound</code> without detail
     * message.
     */
    public GroupNotFound() {
    }

    /**
     * Constructs an instance of <code>groupNotFound</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public GroupNotFound(String msg) {
        super(msg);
    }
}
