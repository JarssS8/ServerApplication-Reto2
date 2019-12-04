/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
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

}
