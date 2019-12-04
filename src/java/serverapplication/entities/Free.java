/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "free", schema = "team6dbreto2")
public class Free extends User implements Serializable{
    private static final long serialVersionUID = 1L;

    private int timeOnline;

    public int getTimeOnline() {
        return timeOnline;
    }

    public void setTimeOnline(int timeOnline) {
        this.timeOnline = timeOnline;
    }
    
}
