/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;



/**
 * Privilege enumeration for the user
 * @author Adrian
 */

public enum Privilege implements Serializable{
    /**
     * If the user can control the application is ADMIN PRIVILEGE
     */
    ADMIN,
    /**
     * If the user access free to our app the user is FREE PRIVILEGE
     */
    FREE,
    /**
     * If the user access paying to our app the user is PREMIUM PRIVILEGE
     */
    PREMIUM
}
