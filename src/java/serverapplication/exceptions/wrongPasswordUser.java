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
public class wrongPasswordUser extends Exception {

    /**
     * Creates a new instance of <code>wrongPasswordUser</code> without detail
     * message.
     */
    public wrongPasswordUser() {
    }

    /**
     * Constructs an instance of <code>wrongPasswordUser</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public wrongPasswordUser(String msg) {
        super(msg);
    }
}