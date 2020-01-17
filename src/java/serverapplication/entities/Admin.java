/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Extends of class User for users that are going to control the platform
 *
 * @author Adrian
 */
@Entity
@Table(name = "admin", schema = "team6dbreto2")
@XmlRootElement
public class Admin extends User implements Serializable {
    
    public Admin() {
        super();
    }
    
    public Admin(User user) {
        super();
        this.setId(user.getId());
        this.setLogin(user.getLogin());
        this.setEmail(user.getEmail());
        this.setFullName(user.getFullName());
        this.setStatus(Status.ENABLED);
        this.setPrivilege(user.getPrivilege());
        this.setLastAccess(user.getLastAccess());
        this.setLastPasswordChange(user.getLastPasswordChange());
        this.setPassword(user.getPassword());
        this.setGroups(user.getGroups());
        this.setDocuments(user.getDocuments());
        this.setRatings(user.getRatings());
        this.setAdminGroups(user.getAdminGroups());
    }

    private static final long serialVersionUID = 1L;
    /**
     * A timestamp with the date of one user become admin
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date adminDate;

    public Date getAdminDate() {
        return adminDate;
    }

    public void setAdminDate(Date adminDate) {
        this.adminDate = adminDate;
    }

    /**
     * Return an int calculated from id for the User
     *
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    /**
     * Compares two instances of Users
     *
     * @param object the other User instance to compare to
     * @return true if instances are equal
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a String representation including id value and classes full Name
     *
     * @return a String of an User id
     */
    @Override
    public String toString() {
        return "serverapplication.entities.User[ id=" + getId() + " ]";
    }
}
