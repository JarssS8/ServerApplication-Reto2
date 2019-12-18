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
public class LoginNotFound extends Exception {

    /**
     * Creates a new instance of <code>loginNotFound</code> without detail
     * message.
     */
    public LoginNotFound() {
    }

    /**
     * Constructs an instance of <code>loginNotFound</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LoginNotFound(String msg) {
        super(msg);
    }
}
