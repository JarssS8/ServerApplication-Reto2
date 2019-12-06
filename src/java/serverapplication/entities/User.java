/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import static javax.persistence.InheritanceType.JOINED;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class is an entity.
 *
 * @author aimar
 */
@Entity
@Inheritance(strategy=JOINED)
@Table(name = "user", schema = "team6dbreto2")
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * The Id for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * The login value for the user.
     */
    @NotNull
    private String login;
    /**
     * The email value for the user.
     */
    @NotNull
    private String email;
    /**
     * The full name for the user.
     */
    private String fullName;
    /**
     * The status for the users account.
     */
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    /**
     * The privilege for the user.
     */
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Privilege privilege;
    /**
     * The password value for the user.
     */
    @NotNull
    private String password;
    /**
     * The date when the user last acceded to the applicacion.
     */
    private Timestamp lastAccess;
    /**
     * The date when the user last changed password.
     */
    private Timestamp lastPasswordChange;
    /**
     * A collection with all the ratings given by the user.
     */
    @OneToMany(mappedBy="user")
    private Set<Rating> ratings;
    /**
     * A collection with all the documents uploaded by the user.
     */
    @OneToMany(mappedBy="user")
    private Set<Document> documents;
    /**
     * A collection with all the groups for the user.
     */
    @ManyToMany
    @JoinTable(name = "user_group", schema = "team6dbreto2")
    private Set<Group> groups;
    /**
     * A collection with the group the user administrates.
     */
    @OneToMany(mappedBy="groupAdmin")
    private Set<Group> adminGroups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Timestamp getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Timestamp lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    @XmlTransient
    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    @XmlTransient
    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @XmlTransient
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the adminGroups
     */
    @XmlTransient
    public Set<Group> getAdminGroups() {
        return adminGroups;
    }

    /**
     * @param adminGroups the adminGroups to set
     */
    public void setAdminGroups(Set<Group> adminGroups) {
        this.adminGroups = adminGroups;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "serverapplication.entities.User[ id=" + id + " ]";
    }

}
