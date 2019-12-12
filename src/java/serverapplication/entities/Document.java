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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@NamedQueries({
    @NamedQuery(
        name="findAllDocuments",
        query="SELECT d FROM Document d ORDER BY d.id ASC"),
     @NamedQuery(
        name="findDocumentByName",
        query="SELECT d FROM Document d WHERE d.name = :name"),
    
    @NamedQuery(
        name="findDocumentNameByName",
        query="SELECT d.name FROM Document d WHERE d.name = :name"),
    @NamedQuery(
        name="findDocumentNameByCategory",
        query="SELECT d.name FROM Document d WHERE d.category = :category"),
    
    @NamedQuery(
        name="findDocumentNameByDate",
        query="SELECT d.name FROM Document d WHERE d.uploadDate = :uploadDate"),
    
    @NamedQuery(
        name="findDocumentNameByNameAndCategory",
        query="SELECT d.name FROM Document d WHERE d.name = :name AND d.category = :category"),
    
    @NamedQuery(
        name="findDocumentNameByNameAndDate",
        query="SELECT d.name FROM Document d WHERE d.name = :name AND d.uploadDate = :uploadDate"),
    
    @NamedQuery(
        name="findDocumentNameByCategoryAndDate",
        query="SELECT d.name FROM Document d WHERE d.category = :category AND d.uploadDate = :uploadDate"),
    
})

/**
 * Entity class for Document.
 * @author Gaizka Andrés
 */
@Entity
@Table(name="document",schema="team6dbreto2")
@XmlRootElement
public class Document implements Serializable{
    private static final long serialVersionUID=1L;
    /**
     * Id to identificate the document
     */
    @EmbeddedId
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private RatingId id;
    /**
     * The name of the document
     */
    @NotNull
    private String name;
    /**
     * The date when the document has been upload
     */
    @NotNull
    private Timestamp uploadDate;
    /**
     * The total rating of the document
     */
    private int totalRating;
    /**
     * The total of reviews the document has
     */
    private int ratingCount;
    /**
     * The file itself
     */
    @NotNull
    @Lob
    private Byte[] file;
    /**
     * The collection of rating the document has been given
     */
    @OneToMany(mappedBy="document")
    private Set<Rating> ratings;
    /**
     * The author of the document
     */
    @ManyToOne
    private User user;
    /**
     * The category of the document
     */
    @ManyToOne
    private Category category;
    /**
     * The author group of the document
     */
    @ManyToOne
    private Group group;
    
    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingAccount) {
        this.ratingCount = ratingAccount;
    }

    public Byte[] getFile() {
        return file;
    }

    public void setFile(Byte[] file) {
        this.file = file;
    }

    @XmlTransient
    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @XmlTransient
       public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
 
 
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    /**
     * Return an int calculated from id for the User
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
     * Obtains a String representation including id value and classes full Name
     * @return a String of an User id
     */
    @Override
    public String toString() {
        return "serverapplication.entities.Document[ id=" + id + " ]";
    }

  

    
}
