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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for rating.
 * 
 * @author Gaizka Andrés
 */
@Entity
@Table(name="rating",schema="team6dbreto2")
public class Rating implements Serializable{
   private static final long serialVersionUID=1L;
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private Long id;
   private int rating;
   private String review;
   private Timestamp ratingDate;
   private Document document;
   
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the review
     */
    public String getReview() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * @return the ratingDate
     */
    public Timestamp getRatingDate() {
        return ratingDate;
    }

    /**
     * @param ratingDate the ratingDate to set
     */
    public void setRatingDate(Timestamp ratingDate) {
        this.ratingDate = ratingDate;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }
    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    /**
     * 
     * @param object
     * @return 
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "serverapplication.entities.Rating[ id=" + getId() + " ]";
    }
}
