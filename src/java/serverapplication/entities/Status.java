/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;


/**
 * Status enumeration for the user
 * @author Adrian
 */
public enum Status implements Serializable{
    /**
     * If user is can access to the application is ENABLED.
     */
    ENABLED,
    /**
     * If user is can NOT access to the application is DISABLED.
     */
    DISABLED
}
