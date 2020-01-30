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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    name="findGroups",
    query="SELECT g FROM Group g"),
    @NamedQuery(
    name="findGroupByNameAndPass",
    query="SELECT g FROM Group g WHERE g.name = :groupName AND g.password = :password"),
    @NamedQuery(
    name="findGroupByName",
    query="SELECT g.name FROM Group g WHERE g.name = :groupName"),
    @NamedQuery(
    name="findGroupById",
    query="SELECT g FROM Group g WHERE g.id = :id"),
    @NamedQuery(
    name="findUsersOfGroup",
    query="SELECT g FROM Group g WHERE g.id = :id")
})

/**
 * Entity of the groups of users
 * @author Diego Urraca
 */
@Entity
@Table(name="group",schema="team6dbreto2")
@XmlRootElement
public class Group implements Serializable{
    private static final long serialVersionUID= 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @ManyToOne
    private User groupAdmin;
    //List of users that are in the group
    @ManyToMany(mappedBy="groups", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<User> users;
    //List of documents that are uploaded by the group
    @OneToMany(mappedBy="group", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Document> documents;

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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the adminId
     */
    public User getGroupAdmin() {
        return groupAdmin;
    }

    /**
     * @param groupAdmin the adminId to set
     */
    public void setGroupAdmin(User groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    /**
     * @return the users
     */
    @XmlTransient
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
     * @return the documents
     */
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(Set<Document> documents) {
        this.setDocuments(documents);
    }
    
    /**
     * Verify if an id is not null
     * @return 0
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /**
     * Verify if object is a Group
     * @param object
     * @return true or false, it depends
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    /**
     * id to String
     * @return the String
     */
    @Override
    public String toString() {
        return "serverapplication.entities.Group[ id=" + id + " ]";
    }
    
}
