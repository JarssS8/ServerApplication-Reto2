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
public class CategoryNameAlreadyExists extends Exception {

    /**
     * Creates a new instance of <code>categoryNameAlreadyExists</code> without
     * detail message.
     */
    public CategoryNameAlreadyExists() {
    }

    /**
     * Constructs an instance of <code>categoryNameAlreadyExists</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoryNameAlreadyExists(String msg) {
        super(msg);
    }
}
