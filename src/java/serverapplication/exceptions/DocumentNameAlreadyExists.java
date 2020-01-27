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
public class DocumentNameAlreadyExists extends Exception {

    /**
     * Creates a new instance of <code>DocumentNameAlreadyExists</code> without
     * detail message.
     */
    public DocumentNameAlreadyExists() {
    }

    /**
     * Constructs an instance of <code>DocumentNameAlreadyExists</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DocumentNameAlreadyExists(String msg) {
        super(msg);
    }
}
