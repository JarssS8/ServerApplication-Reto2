/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for Document.
 * 
 * @author Gaizka Andrés
 */
@Entity
@Table(name="document",schema="team6dbreto2")
public class Document implements Serializable{
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private Timestamp uploadDate;
    private int totalRating;
    private int ratingAccount;
    private Blob file;
    private Set<Rating> ratings;
    private Set<User> users;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the uploadDate
     */
    public Timestamp getUploadDate() {
        return uploadDate;
    }

    /**
     * @param uploadDate the uploadDate to set
     */
    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * @return the totalRating
     */
    public int getTotalRating() {
        return totalRating;
    }

    /**
     * @param totalRating the totalRating to set
     */
    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    /**
     * @return the ratingAccount
     */
    public int getRatingAccount() {
        return ratingAccount;
    }

    /**
     * @param ratingAccount the ratingAccount to set
     */
    public void setRatingAccount(int ratingAccount) {
        this.ratingAccount = ratingAccount;
    }

    /**
     * @return the file
     */
    public Blob getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(Blob file) {
        this.file = file;
    }

    /**
     * @return the ratings
     */
    public Set<Rating> getRatings() {
        return ratings;
    }

    /**
     * @param ratings the ratings to set
     */
    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return "serverapplication.entities.Document[ id=" + id + " ]";
    }
}
