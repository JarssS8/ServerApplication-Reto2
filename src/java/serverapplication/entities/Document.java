 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@NamedQueries({
    @NamedQuery(
        name="findAllDocuments",
        query="SELECT d FROM Document d ORDER BY d.id ASC"),
    @NamedQuery(
        name="findDocumentNameByParameters",
        query="SELECT d FROM Document d WHERE UPPER(d.name) LIKE UPPER(:name) OR d.category = :category OR d.uploadDate = :uploadDate"),
    
    @NamedQuery(
        name="findRatingsOfDocument",
        query="SELECT d FROM Document d WHERE d.id = :id"
    ),
    @NamedQuery(
            name="findDocumentNamebyName",
            query="SELECT d FROM Document d WHERE d.name = :name"
    ),
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * The name of the document
     */
    @NotNull
    private String name;
    /**
     * The date when the document has been upload
     */
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date uploadDate;
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
    //@NotNull
    @Lob
    @Basic(fetch=EAGER)
    private byte[] file;
    /**
     * The collection of rating the document has been given
     */
    @OneToMany(mappedBy= "document", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Rating> ratings;
    /**
     * The author of the document
     */
    @ManyToOne(cascade = {CascadeType.MERGE})
    private User user;
    /**
     * The category of the document
     */
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Category category;
    /**
     * The author group of the document
     */
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Group group;
    
    public Document(){
    }
    
    public Document(Long id,String name, String author, Date uploadDate, int totalRating, int ratingCount){
        this.id=id;
        this.name=name;
        this.user = new User();
        this.user.setLogin(author);
        this.uploadDate=uploadDate;
        this.totalRating=totalRating;
        this.ratingCount=ratingCount;
        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
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
 
    @XmlTransient
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
