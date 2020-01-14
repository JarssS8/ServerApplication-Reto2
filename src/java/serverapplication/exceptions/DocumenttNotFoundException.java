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
public class DocumenttNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DocumentNotFoundException</code> without
     * detail message.
     */
    public DocumenttNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DocumentNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DocumenttNotFoundException(String msg) {
        super(msg);
    }
}
