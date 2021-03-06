/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * Class category, with the different categories for our application. Every
 * document must have a category
 *
 * @author Adrian
 */
@NamedQueries({
    @NamedQuery(
        name="findCategoryByName",
        query="SELECT c FROM Category c WHERE c.name = :name"),
    @NamedQuery(
        name="findDocumentsByCategory",
        query="SELECT c FROM Category c WHERE UPPER(c.name) = UPPER(:name)"),
    @NamedQuery(
        name="findAllCategories",
        query="SELECT c FROM Category c ORDER BY c.name")
})
@Entity
@Table(name = "category", schema = "team6dbreto2")
@XmlRootElement
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * A long with the identifier of the category
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * A String with the name of the category
     */
    @NotNull
    private String name;
    /**
     * A collection with the documents of this category
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private Set<Document> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }
  
    @XmlTransient
    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    /**
     * Return an int calculated from id for the Category
     *
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two instances of Category
     *
     * @param object the other Category instance to compare to
     * @return true if instances are equal
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return "serverapplication.entities.Category[ id=" + id + " ]";
    }

}
