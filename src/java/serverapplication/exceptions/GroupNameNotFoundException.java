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
public class GroupNameNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>GroupNameNotFoundException</code> without
     * detail message.
     */
    public GroupNameNotFoundException() {
    }

    /**
     * Constructs an instance of <code>GroupNameNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupNameNotFoundException(String msg) {
        super(msg);
    }
}
