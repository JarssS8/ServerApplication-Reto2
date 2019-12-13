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
public class GroupPasswordNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>GroupPasswordNotFoundException</code>
     * without detail message.
     */
    public GroupPasswordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>GroupPasswordNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupPasswordNotFoundException(String msg) {
        super(msg);
    }
}
