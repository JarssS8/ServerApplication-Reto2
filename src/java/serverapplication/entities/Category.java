/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "category", schema = "team6dbreto2")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Name;
    @OneToMany(mappedBy = "category", fetch = EAGER)
    private Set<Document> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

}
