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
public class CategoryNameAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of <code>CategoryNameAlreadyExistsException</code>
     * without detail message.
     */
    public CategoryNameAlreadyExistsException() {
    }

    /**
     * Constructs an instance of <code>CategoryNameAlreadyExistsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoryNameAlreadyExistsException(String msg) {
        super(msg);
    }
}
