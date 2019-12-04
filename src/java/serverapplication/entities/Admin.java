/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;

import javax.persistence.Table;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "admin", schema = "team6dbreto2")
public class Admin extends User implements Serializable{
    private static final long serialVersionUID = 1L;

    private Timestamp adminDate;

    public Timestamp getAdminDate() {
        return adminDate;
    }

    public void setAdminDate(Timestamp adminDate) {
        this.adminDate = adminDate;
    }
    
}
