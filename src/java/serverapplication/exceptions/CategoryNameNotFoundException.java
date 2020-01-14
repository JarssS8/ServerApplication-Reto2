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
public class CategoryNameNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CategoryNameNotFoundException</code>
     * without detail message.
     */
    public CategoryNameNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CategoryNameNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoryNameNotFoundException(String msg) {
        super(msg);
    }
}
