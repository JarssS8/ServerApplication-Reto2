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
public class DocumentNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>documentNotFound</code> without detail
     * message.
     */
    public DocumentNotFoundException() {
    }

    /**
     * Constructs an instance of <code>documentNotFound</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}
