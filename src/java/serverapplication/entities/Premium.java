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
 * Extends of class User for users that access paying to our platform
 * @author Adrian
 */
@Entity
@Table(name = "status", schema = "team6dbreto2")
public class Premium extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean autorenovation;
    private Timestamp beginSub;
    private Long cardNumber;
    private int cvc;
    private Timestamp endSub;
    private int expirationMonth;
    private int expirationYear;

    public boolean isAutorenovation() {
        return autorenovation;
    }

    public void setAutorenovation(boolean autorenovation) {
        this.autorenovation = autorenovation;
    }

    public Timestamp getBeginSub() {
        return beginSub;
    }

    public void setBeginSub(Timestamp beginSub) {
        this.beginSub = beginSub;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public Timestamp getEndSub() {
        return endSub;
    }

    public void setEndSub(Timestamp endSub) {
        this.endSub = endSub;
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }

    /**
     * Return an int calculated from id for the User
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
     * @return a String of an User id
     */
    @Override
    public String toString() {
        return "serverapplication.entities.User[ id=" + getId() + " ]";
    }
}
