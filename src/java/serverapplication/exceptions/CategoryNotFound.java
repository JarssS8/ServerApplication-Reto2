/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.exceptions;

/**
 *
 * @author Adrian
 */
public class CategoryNotFound extends Exception {

    /**
     * Creates a new instance of <code>categoryNotFound</code> without detail
     * message.
     */
    public CategoryNotFound() {
    }

    /**
     * Constructs an instance of <code>categoryNotFound</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoryNotFound(String msg) {
        super(msg);
    }
}
