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
public class GroupNameAlreadyExists extends Exception {

    /**
     * Creates a new instance of <code>GroupNameAlreadyExists</code> without
     * detail message.
     */
    public GroupNameAlreadyExists() {
    }

    /**
     * Constructs an instance of <code>GroupNameAlreadyExists</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GroupNameAlreadyExists(String msg) {
        super(msg);
    }
}
