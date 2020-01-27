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
public class GroupIdNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>GorupIdNotFoundException</code> without
     * detail message.
     */
    public GroupIdNotFoundException() {
    }

    /**
     * Constructs an instance of <code>GorupIdNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupIdNotFoundException(String msg) {
        super(msg);
    }
}
